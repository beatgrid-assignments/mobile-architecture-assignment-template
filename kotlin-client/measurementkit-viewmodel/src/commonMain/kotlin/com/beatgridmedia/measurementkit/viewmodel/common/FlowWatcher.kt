package com.beatgridmedia.measurementkit.viewmodel.common

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.beatkit.foundation.BeatKitRegistration

/**
 * Swift friendly wrapper for observing a [StateFlow] (Kotlin flows cannot be collected from Swift directly).
 *
 * ```swift
 * let registration = viewModel.stateWatcher.watch { state in self.state = state }
 * ...
 * registration.unregister()
 * ```
 */
class FlowWatcher<T : Any>(private val flow: StateFlow<T>) {

    val value: T
        get() = flow.value

    /**
     * Invokes [block] on the main thread with the current value and every subsequent change.
     */
    fun watch(block: (T) -> Unit): BeatKitRegistration {
        val scope = MainScope()
        scope.launch { flow.collect { block(it) } }
        return BeatKitRegistration { scope.cancel() }
    }
}
