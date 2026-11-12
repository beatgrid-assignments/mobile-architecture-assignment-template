package org.beatkit.foundation

import org.beatkit.foundation.plugin.BeatKitPlugin
import org.beatkit.foundation.provider.BeatKitProvider

/**
 * A module bundles a set of plugins and providers which together implement a feature area
 * (e.g. core/rest, wallet, authentication).
 */
interface BeatKitModule<S : BeatKitState> {

    val plugins: Iterable<BeatKitPlugin<S>>
        get() = emptyList()

    val providers: Iterable<BeatKitProvider>
        get() = emptyList()

    /**
     * Modules with a lower order are created first.
     */
    val order: Int
        get() = Int.MAX_VALUE
}
