package com.docuten.demo.exceptions;

public class UserIdNotProvidedException extends RuntimeException {
    public UserIdNotProvidedException() {
        super("User id is missing.");
    }

    public UserIdNotProvidedException(String message) {
        super(message);
    }
}
