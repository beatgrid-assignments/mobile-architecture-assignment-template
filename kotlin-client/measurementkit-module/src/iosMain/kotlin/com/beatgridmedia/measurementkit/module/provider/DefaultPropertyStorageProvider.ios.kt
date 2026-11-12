package com.beatgridmedia.measurementkit.module.provider

import platform.Foundation.NSUserDefaults

actual class DefaultPropertyStorageProvider : PropertyStorageProvider {

    private val defaults: NSUserDefaults = NSUserDefaults(suiteName = "measurementkit")

    actual override fun getString(key: String): String? = defaults.stringForKey(key)

    actual override fun putString(key: String, value: String?) {
        if (value == null) defaults.removeObjectForKey(key) else defaults.setObject(value, forKey = key)
    }

    actual override fun clear() {
        defaults.dictionaryRepresentation().keys.forEach { defaults.removeObjectForKey(it as String) }
    }
}
