package com.bunyaminkalkan.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserDataRequestException.class)
    public ResponseEntity<Object> handleIUDRequestException(InvalidUserDataRequestException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Europe/Istanbul"))
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundRequestException.class)
    public ResponseEntity<Object> handleUserNotFoundRequestException(UserNotFoundRequestException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Europe/Istanbul"))
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
