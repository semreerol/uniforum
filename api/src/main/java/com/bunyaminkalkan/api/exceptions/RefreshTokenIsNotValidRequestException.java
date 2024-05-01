package com.bunyaminkalkan.api.exceptions;

public class RefreshTokenIsNotValidRequestException extends RuntimeException{

    public RefreshTokenIsNotValidRequestException(String message) {
        super(message);
    }
}
