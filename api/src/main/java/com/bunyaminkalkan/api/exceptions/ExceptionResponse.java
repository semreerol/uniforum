package com.bunyaminkalkan.api.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private final String message;
    private final ZonedDateTime timestamp;
}
