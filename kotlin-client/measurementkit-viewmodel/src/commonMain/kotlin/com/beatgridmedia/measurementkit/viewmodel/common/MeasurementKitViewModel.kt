package com.beatgridmedia.measurementkit.viewmodel.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * Base class of the shared view models. View models translate UI events into messages for the runtime and expose
 * UI state as [kotlinx.coroutines.flow.StateFlow]s (use [FlowWatcher] to observe those from Swift).
 *
 * The platform owns the life cycle: Android wraps it in an androidx `ViewModel`, iOS in an `ObservableObject`. Both
 * call [onCleared] when the view model is discarded.
 */
abstract class MeasurementKitViewModel {

    protected val scope: CoroutineScope = MainScope()

    open fun onCleared() {
        scope.cancel()
    }
}
