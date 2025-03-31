package com.docuten.demo.exceptions;

public class KeysNotFoundException extends RuntimeException {
    public KeysNotFoundException() {
        super("User keys not found in the database.");
    }

    public KeysNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
