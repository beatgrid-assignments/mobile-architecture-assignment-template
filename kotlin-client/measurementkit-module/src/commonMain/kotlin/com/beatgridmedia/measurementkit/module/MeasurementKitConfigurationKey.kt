package com.beatgridmedia.measurementkit.module

/**
 * Configuration keys which the app supplies when creating the runtime.
 */
enum class MeasurementKitConfigurationKey(val key: String) {

    /**
     * Base URL of the REST server, e.g. `http://10.0.2.2:8080` (Android emulator -> host machine) or
     * `http://localhost:8080` (iOS simulator).
     */
    ServiceBaseUrl("measurementkit.service.baseurl"),

    /**
     * Semantic version of the app, e.g. `1.0.0`.
     */
    ClientVersion("measurementkit.client.version"),

    /**
     * `android` or `ios`.
     */
    ClientPlatform("measurementkit.client.platform"),
}

operator fun Map<String, String>.get(key: MeasurementKitConfigurationKey): String? = this[key.key]
