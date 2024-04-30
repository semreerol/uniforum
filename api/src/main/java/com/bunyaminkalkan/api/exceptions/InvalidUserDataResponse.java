package com.bunyaminkalkan.api.exceptions;


import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class InvalidUserDataResponse {
    private final String message;
    private final ZonedDateTime timestamp;

    public InvalidUserDataResponse(String message, ZonedDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
