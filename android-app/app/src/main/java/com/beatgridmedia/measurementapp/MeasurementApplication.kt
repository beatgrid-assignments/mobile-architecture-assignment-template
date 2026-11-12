package com.beatgridmedia.measurementapp

import android.app.Application
import com.beatgridmedia.measurementkit.module.MeasurementKitConfigurationKey
import com.beatgridmedia.measurementkit.module.MeasurementKitFactory
import com.beatgridmedia.measurementkit.module.MeasurementKitState
import org.beatkit.foundation.BeatKitRuntime
import org.beatkit.foundation.platform.PlatformContext

class MeasurementApplication : Application() {

    lateinit var runtime: BeatKitRuntime<MeasurementKitState>
        private set

    override fun onCreate() {
        super.onCreate()
        runtime = MeasurementKitFactory.createRuntime(
            platformContext = PlatformContext(this),
            configuration = mapOf(
                MeasurementKitConfigurationKey.ServiceBaseUrl.key to BuildConfig.SERVICE_BASE_URL,
                MeasurementKitConfigurationKey.ClientVersion.key to BuildConfig.VERSION_NAME,
                MeasurementKitConfigurationKey.ClientPlatform.key to "android",
            ),
            debug = BuildConfig.DEBUG,
        )
        runtime.start()
    }
}
