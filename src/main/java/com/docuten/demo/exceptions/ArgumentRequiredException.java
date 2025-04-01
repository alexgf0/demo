package com.docuten.demo.exceptions;

public class ArgumentRequiredException extends RuntimeException {
    public ArgumentRequiredException(String message) {
        super(message);
    }

    public ArgumentRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
