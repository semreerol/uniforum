package com.bunyaminkalkan.api.exceptions;


import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ExceptionResponse {
    private final String message;
    private final ZonedDateTime timestamp;

    public ExceptionResponse(String message, ZonedDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
