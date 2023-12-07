package com.ecommerce.myapp.Exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {

        super(message);
    }
}
