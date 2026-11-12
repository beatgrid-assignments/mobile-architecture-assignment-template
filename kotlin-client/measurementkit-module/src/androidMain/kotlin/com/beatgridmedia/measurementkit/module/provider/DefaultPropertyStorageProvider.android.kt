package com.beatgridmedia.measurementkit.module.provider

import android.content.Context
import android.content.SharedPreferences
import org.beatkit.foundation.BeatKitInternalHandle

actual class DefaultPropertyStorageProvider : PropertyStorageProvider {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(context: BeatKitInternalHandle) {
        preferences = context.platformContext.applicationContext
            .getSharedPreferences("measurementkit", Context.MODE_PRIVATE)
    }

    actual override fun getString(key: String): String? = preferences.getString(key, null)

    actual override fun putString(key: String, value: String?) {
        preferences.edit().apply { if (value == null) remove(key) else putString(key, value) }.apply()
    }

    actual override fun clear() {
        preferences.edit().clear().apply()
    }
}
