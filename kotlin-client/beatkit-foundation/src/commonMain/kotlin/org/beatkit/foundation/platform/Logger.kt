package org.beatkit.foundation.platform

object BeatKitLogger {
    var isDebugEnabled: Boolean = true

    fun debug(message: () -> String) {
        if (isDebugEnabled) println("[BeatKit] D ${message()}")
    }

    fun info(message: () -> String) = println("[BeatKit] I ${message()}")

    fun warn(message: () -> String) = println("[BeatKit] W ${message()}")

    fun error(error: Throwable? = null, message: () -> String) {
        println("[BeatKit] E ${message()}" + (error?.let { ": ${it::class.simpleName}: ${it.message}" } ?: ""))
    }
}
