package com.beatgridmedia.measurementkit.viewmodel.login

import com.beatgridmedia.measurementkit.module.MeasurementKitState
import com.beatgridmedia.measurementkit.module.system.ServerStatusMessage
import com.beatgridmedia.measurementkit.viewmodel.common.FlowWatcher
import com.beatgridmedia.measurementkit.viewmodel.common.MeasurementKitViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.beatkit.foundation.BeatKitRuntime

data class LoginUiState(
    val isBusy: Boolean = false,
    /**
     * One-off message to show to the user (e.g. an error).
     */
    val message: String? = null,
    /**
     * Shown in the footer of the login screen, to verify the app <-> server connection.
     */
    val serverStatus: String? = null,
)

/**
 * View model of the login screen.
 *
 * TODO(assignment): login with phone number. Which screens / steps does the flow have and which state do they need?
 */
class LoginViewModel(private val runtime: BeatKitRuntime<MeasurementKitState>) : MeasurementKitViewModel() {

    private val mutableState = MutableStateFlow(LoginUiState())

    val state: StateFlow<LoginUiState> = mutableState.asStateFlow()

    val stateWatcher = FlowWatcher(state)

    init {
        refreshServerStatus()
    }

    fun onLoginWithPhoneNumberClicked() {
        // TODO(assignment)
        mutableState.update { it.copy(message = "Login with phone number is not implemented yet") }
    }

    fun onMessageShown() {
        mutableState.update { it.copy(message = null) }
    }

    fun refreshServerStatus() {
        scope.launch {
            val status = runCatching { runtime.send(ServerStatusMessage()) }
                .fold(
                    onSuccess = { "Server ${it.status} (v${it.version})" },
                    onFailure = { "Server unreachable (${it.message})" },
                )
            mutableState.update { it.copy(serverStatus = status) }
        }
    }
}
