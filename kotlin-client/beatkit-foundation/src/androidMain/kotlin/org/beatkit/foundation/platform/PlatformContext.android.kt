package org.beatkit.foundation.platform

import android.content.Context

actual class PlatformContext(context: Context) {
    val applicationContext: Context = context.applicationContext
}
