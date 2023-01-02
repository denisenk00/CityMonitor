package com.denysenko.citymonitorweb.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException{
    private HttpStatus httpStatus;

    public RestException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public RestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public RestException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public RestException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }
}
