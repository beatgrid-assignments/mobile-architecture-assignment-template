package com.beatgridmedia.measurementkit.module.system

import com.beatgridmedia.measurementkit.model.system.ServerStatus
import com.beatgridmedia.measurementkit.module.MeasurementKitState
import org.beatkit.foundation.plugin.BeatKitMessageResponder
import org.beatkit.module.core.plugin.RestPlugin
import org.beatkit.module.core.provider.RequestConfiguration
import org.beatkit.module.core.provider.get

/**
 * Worked example of a message responder: a plugin that handles [ServerStatusMessage] via the [RestProvider].
 */
class ServerStatusPlugin : RestPlugin<MeasurementKitState>(),
    BeatKitMessageResponder<ServerStatusMessage, ServerStatus> {

    override val messageType = ServerStatusMessage::class

    override suspend fun onMessage(message: ServerStatusMessage): ServerStatus {
        return restProvider.get("api/v1/status", RequestConfiguration.UNAUTHENTICATED)
    }
}
