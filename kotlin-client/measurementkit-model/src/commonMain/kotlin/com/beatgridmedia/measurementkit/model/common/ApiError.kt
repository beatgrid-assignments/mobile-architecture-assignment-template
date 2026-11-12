package com.beatgridmedia.measurementkit.model.common

import kotlinx.serialization.Serializable

/**
 * Error body returned by the server for non 2xx responses.
 */
@Serializable
data class ApiError(
    val code: String,
    val message: String? = null,
)
