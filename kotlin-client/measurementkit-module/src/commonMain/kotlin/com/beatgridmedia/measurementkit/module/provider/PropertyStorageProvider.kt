package com.beatgridmedia.measurementkit.module.provider

import org.beatkit.foundation.provider.BeatKitProvider

/**
 * Simple persistent key/value storage.
 *
 * Worked example of a provider with a platform specific implementation ([DefaultPropertyStorageProvider] is an
 * `expect` class with an `actual` per platform).
 *
 * Note: the default implementations (SharedPreferences / NSUserDefaults) are NOT encrypted.
 */
interface PropertyStorageProvider : BeatKitProvider {

    fun getString(key: String): String?

    fun putString(key: String, value: String?)

    fun clear()
}

expect class DefaultPropertyStorageProvider() : PropertyStorageProvider {
    override fun getString(key: String): String?
    override fun putString(key: String, value: String?)
    override fun clear()
}
