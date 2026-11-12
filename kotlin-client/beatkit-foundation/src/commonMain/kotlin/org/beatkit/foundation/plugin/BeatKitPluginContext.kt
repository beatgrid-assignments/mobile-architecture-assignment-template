package org.beatkit.foundation.plugin

import org.beatkit.foundation.BeatKitInternalHandle
import org.beatkit.foundation.BeatKitState

/**
 * Lets plugins control the runtime.
 */
interface BeatKitPluginContext<S : BeatKitState> : BeatKitInternalHandle {

    val state: S

    /**
     * Whether the plugin is serviced (runtime started and all requirements available).
     */
    val isServiced: Boolean

    /**
     * Transitions the runtime into [state]. All serviced plugins receive `onStateChange`.
     */
    fun updateState(state: S)

    /**
     * Marks the specified tokens as available; plugins for which all requirements are met become serviceable.
     */
    fun setAvailable(vararg token: String)

    /**
     * Revokes previously available tokens.
     */
    fun setUnavailable(vararg token: String)

    fun isAvailable(vararg token: String): Boolean
}
