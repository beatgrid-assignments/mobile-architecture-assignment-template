package org.beatkit.module.core.provider

import io.ktor.client.HttpClient

/**
 * Creates an [HttpClient] backed by the platform's native HTTP stack (OkHttp on Android, NSURLSession on iOS).
 */
expect fun createPlatformHttpClient(): HttpClient
