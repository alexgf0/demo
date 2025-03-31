package com.docuten.demo.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User id not found in the database.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
