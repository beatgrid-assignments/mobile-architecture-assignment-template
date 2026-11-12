package com.beatgridmedia.measurementkit.model.system

import kotlinx.serialization.Serializable

/**
 * Response of `GET /api/v1/status`.
 */
@Serializable
data class ServerStatus(
    val status: String,
    val version: String,
    val serverTime: Long,
)
