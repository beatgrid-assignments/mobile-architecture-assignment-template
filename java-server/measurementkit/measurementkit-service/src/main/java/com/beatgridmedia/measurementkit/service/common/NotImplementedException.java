package com.beatgridmedia.measurementkit.service.common;

/**
 * Thrown by placeholders that still have to be implemented. Mapped to HTTP 501.
 */
public class NotImplementedException extends RuntimeException {

    public NotImplementedException(String message) {
        super(message);
    }
}
