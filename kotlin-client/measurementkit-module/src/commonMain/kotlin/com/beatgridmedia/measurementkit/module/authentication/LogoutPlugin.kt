package com.beatgridmedia.measurementkit.module.authentication

import com.beatgridmedia.measurementkit.module.MeasurementKitState
import com.beatgridmedia.measurementkit.module.provider.PropertyStorageProvider
import org.beatkit.foundation.plugin.BeatKitMessageResponder
import org.beatkit.foundation.plugin.BeatKitPlugin
import org.beatkit.foundation.plugin.BeatKitPluginContext

/**
 * Worked example of a plugin that changes the runtime state.
 *
 * TODO(assignment): what else has to happen on logout (server side and client side)?
 */
class LogoutPlugin : BeatKitPlugin<MeasurementKitState>, BeatKitMessageResponder<LogoutMessage, Unit> {

    private lateinit var context: BeatKitPluginContext<MeasurementKitState>
    private lateinit var propertyStorage: PropertyStorageProvider

    override val messageType = LogoutMessage::class

    override fun onCreate(context: BeatKitPluginContext<MeasurementKitState>) {
        this.context = context
        this.propertyStorage = context.getProvider(PropertyStorageProvider::class)
    }

    override suspend fun onMessage(message: LogoutMessage) {
        propertyStorage.clear()
        context.updateState(MeasurementKitState.LoggedOut)
    }
}
