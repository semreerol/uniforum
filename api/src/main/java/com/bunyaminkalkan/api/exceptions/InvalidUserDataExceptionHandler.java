package com.bunyaminkalkan.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class InvalidUserDataExceptionHandler {

    @ExceptionHandler(value = {InvalidUserDataRequestException.class})
    public ResponseEntity<Object> handleIUDRequestException(InvalidUserDataException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        InvalidUserDataException invalidUserDataException = new InvalidUserDataException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Europe/Istanbul"))
        );
        return new ResponseEntity<>(invalidUserDataException, badRequest);
    }
}
