package org.beatkit.foundation

import kotlin.uuid.Uuid

/**
 * A request sent through the runtime with [BeatKitHandle.send]. Exactly one
 * [org.beatkit.foundation.plugin.BeatKitMessageResponder] handles a given message type and produces a result of type [R].
 *
 * Messages are the public API of the middleware: the apps (via the view models) only ever talk to the middleware by
 * sending messages and subscribing to topics. They never call plugins or providers directly.
 */
abstract class BeatKitMessage<out R : Any> {
    val uuid: String = Uuid.random().toString()

    override fun toString(): String = "${this::class.simpleName}(uuid=$uuid)"
}
