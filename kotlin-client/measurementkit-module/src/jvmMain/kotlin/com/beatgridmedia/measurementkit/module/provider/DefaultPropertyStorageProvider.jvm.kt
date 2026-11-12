package com.beatgridmedia.measurementkit.module.provider

import java.util.concurrent.ConcurrentHashMap

/**
 * In memory implementation, the JVM target is only used for unit tests.
 */
actual class DefaultPropertyStorageProvider : PropertyStorageProvider {

    private val values = ConcurrentHashMap<String, String>()

    actual override fun getString(key: String): String? = values[key]

    actual override fun putString(key: String, value: String?) {
        if (value == null) values.remove(key) else values[key] = value
    }

    actual override fun clear() = values.clear()
}
