package com.bunyaminkalkan.api.exceptions;

public class UserNotFoundRequestException extends RuntimeException{

    public UserNotFoundRequestException(String message) {
        super(message);
    }
}
