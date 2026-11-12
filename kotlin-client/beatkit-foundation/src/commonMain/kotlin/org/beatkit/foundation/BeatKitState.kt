package org.beatkit.foundation

/**
 * The top level state of a BeatKit runtime, e.g. `LoggedOut` / `LoggedIn`. Every app defines its own (enum) type.
 */
interface BeatKitState {
    val name: String

    /**
     * If true, plugins receive [org.beatkit.foundation.plugin.BeatKitPlugin.onReset] when the runtime enters this state
     * (e.g. when a user is logged out, all user related state has to be wiped).
     */
    val shouldReset: Boolean
        get() = false
}
