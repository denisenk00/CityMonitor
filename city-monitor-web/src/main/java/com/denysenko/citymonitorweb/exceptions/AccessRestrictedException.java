package com.denysenko.citymonitorweb.exceptions;

public class AccessRestrictedException extends RuntimeException{
    public AccessRestrictedException() {
    }

    public AccessRestrictedException(String message) {
        super(message);
    }

    public AccessRestrictedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessRestrictedException(Throwable cause) {
        super(cause);
    }
}
