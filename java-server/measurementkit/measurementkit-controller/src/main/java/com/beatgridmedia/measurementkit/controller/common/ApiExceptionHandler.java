package com.beatgridmedia.measurementkit.controller.common;

import com.beatgridmedia.measurementkit.service.common.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotImplementedException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ApiError handleNotImplemented(NotImplementedException e) {
        return new ApiError("not_implemented", e.getMessage());
    }
}
