package org.beatkit.module.core.provider

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json
import org.beatkit.foundation.platform.BeatKitLogger

/**
 * Ktor based [RestProvider]. The HTTP engine is platform specific (OkHttp on Android, NSURLSession on iOS), see
 * [createPlatformHttpClient].
 *
 * @param baseUrl e.g. `http://10.0.2.2:8080` (Android emulator) or `http://localhost:8080` (iOS simulator).
 * @param httpClientFactory overridable for tests (e.g. with a Ktor `MockEngine`).
 */
open class DefaultRestProvider(
    baseUrl: String,
    httpClientFactory: () -> HttpClient = { createPlatformHttpClient() },
) : RestProvider {

    override val serverBaseUrl: String = baseUrl.trimEnd('/')

    protected val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    protected val client: HttpClient = httpClientFactory().config {
        expectSuccess = false
        install(ContentNegotiation) { json(json) }
        install(Logging) {
            level = LogLevel.INFO
            logger = object : Logger {
                override fun log(message: String) = BeatKitLogger.debug { message }
            }
        }
        defaultRequest { url("$serverBaseUrl/") }
    }

    override suspend fun <R : Any> get(path: String, responseType: TypeInfo, configuration: RequestConfiguration): R {
        val response = client.get(path.trimStart('/')) { prepare(configuration) }
        return handle(response, responseType)
    }

    override suspend fun <T : Any, R : Any> post(
        path: String,
        entity: T,
        requestType: TypeInfo,
        responseType: TypeInfo,
        configuration: RequestConfiguration,
    ): R {
        val response = client.post(path.trimStart('/')) {
            prepare(configuration)
            contentType(ContentType.Application.Json)
            setBody(entity, requestType)
        }
        return handle(response, responseType)
    }

    override suspend fun delete(path: String, configuration: RequestConfiguration) {
        val response = client.delete(path.trimStart('/')) { prepare(configuration) }
        handle<Unit>(response, null)
    }

    /**
     * Hook invoked for every request.
     *
     * TODO(assignment): this is where authenticated requests would get their credentials attached. How does the
     *  provider obtain them, where are they stored, what happens when they are expired?
     */
    protected open suspend fun HttpRequestBuilder.prepare(configuration: RequestConfiguration) {
    }

    /**
     * TODO(assignment): how should an unauthorized (401) response be handled? Retry? Refresh? Log out?
     */
    protected open suspend fun <R> handle(response: HttpResponse, responseType: TypeInfo?): R {
        if (!response.status.isSuccess()) {
            throw RestException(response.status.value, runCatching { response.bodyAsText() }.getOrNull())
        }
        @Suppress("UNCHECKED_CAST")
        return if (responseType == null) Unit as R else response.body(responseType)
    }
}
