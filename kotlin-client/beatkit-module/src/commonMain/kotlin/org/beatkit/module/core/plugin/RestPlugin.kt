package org.beatkit.module.core.plugin

import org.beatkit.foundation.BeatKitState
import org.beatkit.foundation.plugin.BeatKitPlugin
import org.beatkit.foundation.plugin.BeatKitPluginContext
import org.beatkit.module.core.provider.RestProvider

/**
 * Base class for plugins that talk to the server.
 */
abstract class RestPlugin<S : BeatKitState> : BeatKitPlugin<S> {

    protected lateinit var restProvider: RestProvider
    protected lateinit var context: BeatKitPluginContext<S>

    override fun onCreate(context: BeatKitPluginContext<S>) {
        this.context = context
        this.restProvider = context.getProvider(RestProvider::class)
    }
}
