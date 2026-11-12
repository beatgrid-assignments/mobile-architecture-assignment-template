package com.beatgridmedia.measurementkit.module.system

import com.beatgridmedia.measurementkit.model.system.ServerStatus
import org.beatkit.foundation.BeatKitMessage

/**
 * Retrieves the status of the server. Does not require authentication.
 */
class ServerStatusMessage : BeatKitMessage<ServerStatus>()
