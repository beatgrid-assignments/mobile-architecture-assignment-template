package org.beatkit.foundation.plugin

import org.beatkit.foundation.BeatKitState

/**
 * Plugins contain the (platform independent) business logic of the middleware. A plugin typically:
 * - responds to messages (see [BeatKitMessageResponder]),
 * - reacts to state changes / errors,
 * - uses providers (looked up via the context in [onCreate]) for I/O and platform specific functionality.
 */
interface BeatKitPlugin<S : BeatKitState> {

    /**
     * First callback, invoked when the runtime is created. Look up providers here.
     */
    fun onCreate(context: BeatKitPluginContext<S>) {}

    /**
     * Invoked when the plugin becomes serviceable: the runtime is started and all [requirements] are available.
     */
    fun onService() {}

    /**
     * Invoked when the runtime state changed (only when serviced).
     */
    fun onStateChange(fromState: S, toState: S) {}

    /**
     * Invoked when the runtime enters a state for which [BeatKitState.shouldReset] is true: clear any user related state.
     */
    fun onReset() {}

    /**
     * Invoked for errors raised by responders or submitted via the context. Return true if handled.
     */
    fun handleError(error: Throwable): Boolean = false

    /**
     * Tokens that have to be available (see [BeatKitPluginContext.setAvailable]) for this plugin to become serviceable.
     */
    val requirements: Array<String>
        get() = arrayOf()
}
