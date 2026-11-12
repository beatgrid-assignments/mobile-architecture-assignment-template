package com.beatgridmedia.measurementkit.controller.common;

/**
 * Error body for non 2xx responses (client counterpart: {@code com.beatgridmedia.measurementkit.model.common.ApiError}).
 */
public record ApiError(String code, String message) {
}
