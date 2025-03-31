package com.docuten.demo.exceptions;

public class SignatureNotProvidedException extends RuntimeException {
    public SignatureNotProvidedException() {
        super("The signature was not provided correctly.");
    }

    public SignatureNotProvidedException(String message) {
        super(message);
    }
}
