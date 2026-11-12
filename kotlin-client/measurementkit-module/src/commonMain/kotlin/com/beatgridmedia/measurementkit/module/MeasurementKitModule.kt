package com.beatgridmedia.measurementkit.module

import com.beatgridmedia.measurementkit.module.authentication.LogoutPlugin
import com.beatgridmedia.measurementkit.module.provider.DefaultPropertyStorageProvider
import com.beatgridmedia.measurementkit.module.system.ServerStatusPlugin
import org.beatkit.foundation.BeatKitModule
import org.beatkit.foundation.plugin.BeatKitPlugin
import org.beatkit.foundation.provider.BeatKitProvider
import org.beatkit.module.core.provider.DefaultRestProvider

/**
 * The app's module: registers all plugins and providers.
 *
 * TODO(assignment): register the plugins / providers for login with phone number here.
 */
class MeasurementKitModule(
    configuration: Map<String, String>,
    additionalProviders: List<BeatKitProvider> = emptyList(),
) : BeatKitModule<MeasurementKitState> {

    override val providers: Iterable<BeatKitProvider> = listOf(
        DefaultRestProvider(
            baseUrl = requireNotNull(configuration[MeasurementKitConfigurationKey.ServiceBaseUrl]) {
                "Missing configuration ${MeasurementKitConfigurationKey.ServiceBaseUrl.key}"
            },
        ),
        DefaultPropertyStorageProvider(),
    ) + additionalProviders

    override val plugins: Iterable<BeatKitPlugin<MeasurementKitState>> = listOf(
        ServerStatusPlugin(),
        LogoutPlugin(),
    )

    override val order: Int
        get() = 10
}
