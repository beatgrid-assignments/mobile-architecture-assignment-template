package com.beatgridmedia.measurementkit.module

import org.beatkit.foundation.BeatKitState

/**
 * Top level state of the app's runtime.
 */
enum class MeasurementKitState : BeatKitState {
    LoggedOut,
    LoggedIn;

    override val shouldReset: Boolean
        get() = this == LoggedOut
}
