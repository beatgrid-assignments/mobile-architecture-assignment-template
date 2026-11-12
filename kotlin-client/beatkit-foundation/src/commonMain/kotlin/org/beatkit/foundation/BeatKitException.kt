package org.beatkit.foundation

open class BeatKitException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Raised when a message is sent for which no responder is registered or for which the responder is not serviceable
 * (its requirements are not available).
 */
class BeatKitUnavailableException(message: String) : BeatKitException(message)
