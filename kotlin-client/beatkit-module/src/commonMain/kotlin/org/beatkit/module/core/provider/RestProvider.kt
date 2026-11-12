package org.beatkit.module.core.provider

import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import org.beatkit.foundation.provider.BeatKitProvider

data class RequestConfiguration(
    /**
     * Whether the request requires an authenticated user.
     */
    val authenticated: Boolean = true,
) {
    companion object {
        val DEFAULT = RequestConfiguration()
        val UNAUTHENTICATED = RequestConfiguration(authenticated = false)
    }
}

/**
 * Raised for non 2xx responses.
 */
class RestException(val statusCode: Int, val body: String?) : Exception("HTTP $statusCode: ${body?.take(200)}")

/**
 * Provider responsible for executing REST calls against the server.
 */
interface RestProvider : BeatKitProvider {

    val serverBaseUrl: String

    suspend fun <R : Any> get(
        path: String,
        responseType: TypeInfo,
        configuration: RequestConfiguration = RequestConfiguration.DEFAULT,
    ): R

    suspend fun <T : Any, R : Any> post(
        path: String,
        entity: T,
        requestType: TypeInfo,
        responseType: TypeInfo,
        configuration: RequestConfiguration = RequestConfiguration.DEFAULT,
    ): R

    suspend fun delete(
        path: String,
        configuration: RequestConfiguration = RequestConfiguration.DEFAULT,
    )
}

suspend inline fun <reified R : Any> RestProvider.get(
    path: String,
    configuration: RequestConfiguration = RequestConfiguration.DEFAULT,
): R = get(path, typeInfo<R>(), configuration)

suspend inline fun <reified T : Any, reified R : Any> RestProvider.post(
    path: String,
    entity: T,
    configuration: RequestConfiguration = RequestConfiguration.DEFAULT,
): R = post(path, entity, typeInfo<T>(), typeInfo<R>(), configuration)
