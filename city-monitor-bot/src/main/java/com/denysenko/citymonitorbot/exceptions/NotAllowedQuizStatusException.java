package com.denysenko.citymonitorbot.exceptions;

public class NotAllowedQuizStatusException extends RuntimeException {
    public NotAllowedQuizStatusException() {
    }

    public NotAllowedQuizStatusException(String message) {
        super(message);
    }

    public NotAllowedQuizStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowedQuizStatusException(Throwable cause) {
        super(cause);
    }
}
