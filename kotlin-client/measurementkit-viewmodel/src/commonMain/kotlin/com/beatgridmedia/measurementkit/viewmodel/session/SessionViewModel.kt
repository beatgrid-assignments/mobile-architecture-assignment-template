package com.beatgridmedia.measurementkit.viewmodel.session

import com.beatgridmedia.measurementkit.module.MeasurementKitState
import com.beatgridmedia.measurementkit.module.authentication.LogoutMessage
import com.beatgridmedia.measurementkit.viewmodel.common.FlowWatcher
import com.beatgridmedia.measurementkit.viewmodel.common.MeasurementKitViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.beatkit.foundation.BeatKitRuntime

/**
 * Root view model: the apps show the login flow or the home screen depending on [state].
 */
class SessionViewModel(private val runtime: BeatKitRuntime<MeasurementKitState>) : MeasurementKitViewModel() {

    val state: StateFlow<MeasurementKitState> = runtime.stateFlow

    val stateWatcher = FlowWatcher(state)

    fun logout() {
        scope.launch { runtime.send(LogoutMessage()) }
    }
}
