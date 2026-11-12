package org.beatkit.foundation.plugin

import org.beatkit.foundation.BeatKitMessage
import kotlin.reflect.KClass

/**
 * Implemented by plugins that handle a message type. Exactly one responder may be registered per message type.
 */
interface BeatKitMessageResponder<T : BeatKitMessage<R>, R : Any> {

    val messageType: KClass<T>

    /**
     * Handles the message. Only invoked while the plugin is serviced.
     */
    suspend fun onMessage(message: T): R
}
