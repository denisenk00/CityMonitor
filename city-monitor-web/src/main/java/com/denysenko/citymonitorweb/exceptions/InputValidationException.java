package com.denysenko.citymonitorweb.exceptions;

public class InputValidationException extends RuntimeException{
    public InputValidationException() {
    }

    public InputValidationException(String message) {
        super(message);
    }

    public InputValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputValidationException(Throwable cause) {
        super(cause);
    }
}
