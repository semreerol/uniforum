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
    public ResponseEntity<Object> handleInvalidUserDataRequestException(InvalidUserDataRequestException e) {
        return new ResponseEntity<>(createExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundRequestException.class)
    public ResponseEntity<Object> handleUserNotFoundRequestException(UserNotFoundRequestException e) {
        return new ResponseEntity<>(createExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenIsNotValidRequestException.class)
    public ResponseEntity<Object> handleRefreshTokenIsNotValidRequestException(RefreshTokenIsNotValidRequestException e) {
        return new ResponseEntity<>(createExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private Object createExceptionResponse(String message) {
        return new ExceptionResponse(
                message,
                ZonedDateTime.now(ZoneId.of("Europe/Istanbul"))
        );
    }
}
