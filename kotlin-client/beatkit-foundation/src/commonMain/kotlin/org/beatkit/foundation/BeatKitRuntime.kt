package org.beatkit.foundation

import kotlinx.coroutines.flow.StateFlow
import org.beatkit.foundation.platform.PlatformContext
import kotlin.reflect.KClass

/**
 * The runtime which hosts all modules (plugins + providers). Apps create exactly one instance.
 */
interface BeatKitRuntime<S : BeatKitState> : BeatKitHandle {

    val state: S

    val stateFlow: StateFlow<S>

    /**
     * Registers a listener for state changes. Convenient from Swift; Kotlin code can use [stateFlow].
     */
    fun observeState(listener: (S) -> Unit): BeatKitRegistration

    /**
     * Starts the runtime: plugins become serviced (and receive `onService`) once their requirements are available.
     */
    fun start()

    /**
     * Releases all resources. The runtime cannot be used afterwards.
     */
    fun close()
}

/**
 * Creates a runtime for the given modules.
 */
fun <S : BeatKitState> BeatKitRuntime(
    modules: Iterable<BeatKitModule<S>>,
    platformContext: PlatformContext,
    initialState: S,
    configuration: Map<String, String> = emptyMap(),
    debug: Boolean = false,
): BeatKitRuntime<S> = BeatKitRuntimeBase(modules, platformContext, initialState, configuration, debug)


@Suppress("UNCHECKED_CAST")
internal fun <T : Any> KClass<T>.castOrNull(value: Any): T? = if (isInstance(value)) value as T else null
