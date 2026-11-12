package org.beatkit.foundation

import kotlinx.coroutines.CoroutineScope
import org.beatkit.foundation.common.AsyncResult
import org.beatkit.foundation.platform.PlatformContext
import org.beatkit.foundation.provider.BeatKitProvider
import kotlin.reflect.KClass

/**
 * The public API of the runtime, used by apps / view models.
 */
interface BeatKitHandle {

    val isDebug: Boolean

    /**
     * The current configuration (key/value) held by the runtime. See e.g. `MeasurementKitConfigurationKey`.
     */
    val configuration: Map<String, String>

    /**
     * Sends a message, suspending until the responder produced a result.
     */
    suspend fun <R : Any> send(message: BeatKitMessage<R>): R

    /**
     * Callback variant of [send] (convenient from Swift). The completion is invoked on the main thread.
     */
    fun <R : Any> send(message: BeatKitMessage<R>, completion: (AsyncResult<R>) -> Unit)

    /**
     * Registers [subscriber] for [topic]. If [receiveLastDataUponSubscription] is true the last published value (if any)
     * is delivered immediately.
     */
    fun <T : Any> subscribe(
        topic: BeatKitTopic<T>,
        receiveLastDataUponSubscription: Boolean = false,
        subscriber: (T) -> Unit,
    ): BeatKitRegistration

    /**
     * Publishes [data] to all subscribers of [topic].
     */
    fun <T : Any> publish(topic: BeatKitTopic<T>, data: T)
}

/**
 * The API available to plugins and providers (not to apps).
 */
interface BeatKitInternalHandle : BeatKitHandle {

    val platformContext: PlatformContext

    /**
     * The default coroutine scope of the runtime. Cancelled when the runtime is closed.
     */
    val scope: CoroutineScope

    /**
     * Looks up the (single) registered provider implementing [providerClass].
     */
    fun <T : BeatKitProvider> getProvider(providerClass: KClass<T>): T

    /**
     * Submits an error which is offered to all serviced plugins via `handleError`.
     */
    fun submitError(error: Throwable)
}
