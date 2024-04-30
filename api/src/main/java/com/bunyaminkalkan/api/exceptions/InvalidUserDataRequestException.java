package com.bunyaminkalkan.api.exceptions;

public class InvalidUserDataRequestException extends RuntimeException{

    public InvalidUserDataRequestException(String message) {
        super(message);
    }
}
