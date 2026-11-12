package com.beatgridmedia.measurementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beatgridmedia.measurementapp.ui.HomeScreen
import com.beatgridmedia.measurementapp.ui.LoginScreen
import com.beatgridmedia.measurementapp.ui.sharedViewModel
import com.beatgridmedia.measurementkit.module.MeasurementKitState
import com.beatgridmedia.measurementkit.viewmodel.login.LoginViewModel
import com.beatgridmedia.measurementkit.viewmodel.session.SessionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val runtime = (application as MeasurementApplication).runtime
        setContent {
            MaterialTheme {
                val sessionViewModel = sharedViewModel { SessionViewModel(runtime) }
                val state = sessionViewModel.state.collectAsStateWithLifecycle()
                when (state.value) {
                    MeasurementKitState.LoggedOut -> LoginScreen(sharedViewModel { LoginViewModel(runtime) })
                    MeasurementKitState.LoggedIn -> HomeScreen(onLogout = sessionViewModel::logout)
                }
            }
        }
    }
}
