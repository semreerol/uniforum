package com.bunyaminkalkan.api.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(createExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(createExceptionResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(createExceptionResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(createExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    private ResponseEntity<Object> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException e){
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Validation error occurred.");
        return new ResponseEntity<>(createExceptionResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    private Object createExceptionResponse(String message) {
        return new ExceptionResponse(
                message,
                ZonedDateTime.now(ZoneId.of("Europe/Istanbul"))
        );
    }


}
