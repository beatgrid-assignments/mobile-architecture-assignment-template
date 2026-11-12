package com.beatgridmedia.measurementkit.module

import org.beatkit.foundation.BeatKitModule
import org.beatkit.foundation.BeatKitRuntime
import org.beatkit.foundation.platform.PlatformContext
import org.beatkit.foundation.provider.BeatKitProvider

/**
 * Entry point for the apps (Android: `MeasurementApplication`, iOS: `MeasurementApp.swift`).
 */
object MeasurementKitFactory {

    /**
     * @param additionalProviders providers implemented by the app itself (e.g. in Swift, for functionality that is
     * easier to implement natively than through Kotlin/Native interop).
     */
    fun createRuntime(
        platformContext: PlatformContext,
        configuration: Map<String, String>,
        additionalProviders: List<BeatKitProvider> = emptyList(),
        debug: Boolean = true,
    ): BeatKitRuntime<MeasurementKitState> {
        val modules: List<BeatKitModule<MeasurementKitState>> = listOf(
            MeasurementKitModule(configuration, additionalProviders),
        )
        return BeatKitRuntime(modules, platformContext, MeasurementKitState.LoggedOut, configuration, debug)
    }
}
