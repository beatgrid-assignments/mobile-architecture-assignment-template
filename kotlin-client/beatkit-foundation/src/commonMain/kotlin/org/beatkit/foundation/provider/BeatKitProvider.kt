package org.beatkit.foundation.provider

import org.beatkit.foundation.BeatKitInternalHandle

/**
 * Providers abstract I/O and platform specific functionality behind an interface (REST, storage, key store, SMS
 * retrieval, ...). Plugins depend on provider *interfaces*; the implementation is either common code or supplied per
 * platform (expect/actual in androidMain / iosMain, or injected by the app).
 *
 * This keeps plugins platform independent and testable (tests register fake providers).
 */
interface BeatKitProvider {

    fun onCreate(context: BeatKitInternalHandle) {}
}
