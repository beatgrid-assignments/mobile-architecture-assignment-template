package org.beatkit.foundation

/**
 * A publish/subscribe channel for data of type [T] (e.g. "the current user changed").
 */
open class BeatKitTopic<T : Any>(val name: String) {
    override fun toString(): String = "BeatKitTopic($name)"
}

fun interface BeatKitRegistration {
    fun unregister()
}
