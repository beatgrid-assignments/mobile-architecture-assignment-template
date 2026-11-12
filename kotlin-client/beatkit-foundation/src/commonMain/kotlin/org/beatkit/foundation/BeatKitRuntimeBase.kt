package org.beatkit.foundation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatkit.foundation.common.AsyncResult
import org.beatkit.foundation.platform.BeatKitLogger
import org.beatkit.foundation.platform.PlatformContext
import org.beatkit.foundation.plugin.BeatKitMessageResponder
import org.beatkit.foundation.plugin.BeatKitPlugin
import org.beatkit.foundation.plugin.BeatKitPluginContext
import org.beatkit.foundation.provider.BeatKitProvider
import kotlin.reflect.KClass

/**
 * Default runtime implementation.
 *
 * Deliberately simple (compared to the production runtime there is no background message queue, no permission handling
 * and no timers), but it has the same programming model: modules -> plugins + providers, messages -> responders,
 * availability tokens -> serviceability, topics, and a top level state.
 *
 * @param callbackDispatcher dispatcher on which callbacks to the app (completions, subscribers, state observers) are
 * invoked. The main thread by default; tests use an unconfined/test dispatcher.
 */
class BeatKitRuntimeBase<S : BeatKitState>(
    modules: Iterable<BeatKitModule<S>>,
    override val platformContext: PlatformContext,
    initialState: S,
    configuration: Map<String, String> = emptyMap(),
    override val isDebug: Boolean = false,
    private val callbackDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : BeatKitRuntime<S>, BeatKitInternalHandle {

    override val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val configuration: Map<String, String> = configuration.toMap()

    private val sortedModules = modules.sortedBy { it.order }
    private val providers: List<BeatKitProvider> = sortedModules.flatMap { it.providers }
    private val plugins: List<PluginEntry<S>> = sortedModules.flatMap { it.plugins }.map { PluginEntry(it, this) }
    private val responders: Map<KClass<*>, PluginEntry<S>>

    private val mutableState = MutableStateFlow(initialState)
    private val started = MutableStateFlow(false)
    private val availableTokens = MutableStateFlow<Set<String>>(emptySet())
    private val topics = MutableStateFlow<Map<BeatKitTopic<*>, TopicState>>(emptyMap())

    override val state: S
        get() = mutableState.value

    override val stateFlow: StateFlow<S> = mutableState.asStateFlow()

    init {
        BeatKitLogger.isDebugEnabled = isDebug
        val responderMap = mutableMapOf<KClass<*>, PluginEntry<S>>()
        for (entry in plugins) {
            val responder = entry.plugin as? BeatKitMessageResponder<*, *> ?: continue
            val existing = responderMap.put(responder.messageType, entry)
            check(existing == null) {
                "Multiple responders registered for ${responder.messageType.simpleName}: " +
                    "${existing?.plugin?.let { it::class.simpleName }} and ${entry.plugin::class.simpleName}"
            }
        }
        responders = responderMap
        providers.forEach { it.onCreate(this) }
        plugins.forEach { it.plugin.onCreate(it) }
    }

    override fun start() {
        if (!started.compareAndSet(expect = false, update = true)) return
        BeatKitLogger.debug { "Runtime started in state ${state.name}" }
        evaluateServiceability()
    }

    override fun close() {
        scope.cancel()
    }

    // region Messages

    override suspend fun <R : Any> send(message: BeatKitMessage<R>): R {
        val entry = responders[message::class]
            ?: throw BeatKitUnavailableException("No responder registered for ${message::class.simpleName}")
        if (!entry.isServiced) {
            throw BeatKitUnavailableException(
                "Responder ${entry.plugin::class.simpleName} for ${message::class.simpleName} is not serviceable " +
                    "(started=${started.value}, requirements=${entry.plugin.requirements.toList()})"
            )
        }
        @Suppress("UNCHECKED_CAST")
        val responder = entry.plugin as BeatKitMessageResponder<BeatKitMessage<R>, R>
        BeatKitLogger.debug { "Sending $message to ${entry.plugin::class.simpleName}" }
        return try {
            responder.onMessage(message)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            BeatKitLogger.error(e) { "$message failed" }
            submitError(e)
            throw e
        }
    }

    override fun <R : Any> send(message: BeatKitMessage<R>, completion: (AsyncResult<R>) -> Unit) {
        scope.launch {
            val result: AsyncResult<R> = try {
                AsyncResult.Success(send(message))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                AsyncResult.Failure(e)
            }
            withContext(callbackDispatcher) { completion(result) }
        }
    }

    // endregion

    // region Topics

    private class TopicState(val lastValue: Any?, val subscribers: List<(Any) -> Unit>)

    override fun <T : Any> subscribe(
        topic: BeatKitTopic<T>,
        receiveLastDataUponSubscription: Boolean,
        subscriber: (T) -> Unit,
    ): BeatKitRegistration {
        @Suppress("UNCHECKED_CAST")
        val untyped = subscriber as (Any) -> Unit
        var lastValue: Any? = null
        topics.update { current ->
            val topicState = current[topic] ?: TopicState(null, emptyList())
            lastValue = topicState.lastValue
            current + (topic to TopicState(topicState.lastValue, topicState.subscribers + untyped))
        }
        if (receiveLastDataUponSubscription) {
            lastValue?.let { value -> scope.launch(callbackDispatcher) { untyped(value) } }
        }
        return BeatKitRegistration {
            topics.update { current ->
                val topicState = current[topic] ?: return@update current
                current + (topic to TopicState(topicState.lastValue, topicState.subscribers - untyped))
            }
        }
    }

    override fun <T : Any> publish(topic: BeatKitTopic<T>, data: T) {
        var subscribers: List<(Any) -> Unit> = emptyList()
        topics.update { current ->
            val topicState = current[topic] ?: TopicState(null, emptyList())
            subscribers = topicState.subscribers
            current + (topic to TopicState(data, topicState.subscribers))
        }
        scope.launch(callbackDispatcher) { subscribers.forEach { it(data) } }
    }

    // endregion

    // region State

    override fun observeState(listener: (S) -> Unit): BeatKitRegistration {
        val job = scope.launch(callbackDispatcher) { mutableState.collect { listener(it) } }
        return BeatKitRegistration { job.cancel() }
    }

    internal fun updateState(newState: S) {
        val oldState = mutableState.value
        if (oldState == newState) return
        mutableState.value = newState
        BeatKitLogger.debug { "State changed: ${oldState.name} -> ${newState.name}" }
        if (newState.shouldReset) {
            plugins.forEach { it.plugin.onReset() }
        }
        plugins.filter { it.isServiced }.forEach { it.plugin.onStateChange(oldState, newState) }
    }

    // endregion

    // region Availability

    internal fun setAvailable(tokens: Array<out String>) {
        availableTokens.update { it + tokens }
        evaluateServiceability()
    }

    internal fun setUnavailable(tokens: Array<out String>) {
        availableTokens.update { it - tokens.toSet() }
        evaluateServiceability()
    }

    internal fun isAvailable(tokens: Array<out String>): Boolean = availableTokens.value.containsAll(tokens.toList())

    private fun evaluateServiceability() {
        val isStarted = started.value
        val tokens = availableTokens.value
        for (entry in plugins) {
            val serviceable = isStarted && tokens.containsAll(entry.plugin.requirements.toList())
            if (entry.serviced.compareAndSet(!serviceable, serviceable) && serviceable) {
                BeatKitLogger.debug { "${entry.plugin::class.simpleName} serviced" }
                entry.plugin.onService()
            }
        }
    }

    // endregion

    // region Providers & errors

    override fun <T : BeatKitProvider> getProvider(providerClass: KClass<T>): T {
        return providers.firstNotNullOfOrNull { providerClass.castOrNull(it) }
            ?: throw BeatKitUnavailableException("No provider registered implementing ${providerClass.simpleName}")
    }

    override fun submitError(error: Throwable) {
        val handled = plugins.filter { it.isServiced }.any { it.plugin.handleError(error) }
        if (!handled) BeatKitLogger.debug { "Unhandled error: ${error::class.simpleName}: ${error.message}" }
    }

    // endregion

    /**
     * The context handed to an individual plugin.
     */
    private class PluginEntry<S : BeatKitState>(
        val plugin: BeatKitPlugin<S>,
        private val runtime: BeatKitRuntimeBase<S>,
    ) : BeatKitPluginContext<S>, BeatKitInternalHandle by runtime {

        val serviced = MutableStateFlow(false)

        override val state: S
            get() = runtime.state

        override val isServiced: Boolean
            get() = serviced.value

        override fun updateState(state: S) = runtime.updateState(state)

        override fun setAvailable(vararg token: String) = runtime.setAvailable(token)

        override fun setUnavailable(vararg token: String) = runtime.setUnavailable(token)

        override fun isAvailable(vararg token: String): Boolean = runtime.isAvailable(token)
    }
}
