package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.exceptions.RestException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ValidationException;

@Log4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final String INTERNAL_SERVER_ERROR_MESSAGE = "Внутрішня помилка сервера. Будь-ласка, зверніться до адміністратора";

    @ExceptionHandler(Exception.class)
    public String handleInternalServerError(Exception e, Model model) {
        e.printStackTrace();
        log.error("Exception raised: ", e);
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);
        model.addAttribute("message", INTERNAL_SERVER_ERROR_MESSAGE);
        return "custom-error";
    }

    @ExceptionHandler({RestException.class})
    public ResponseEntity handleRestException(RestException e) {
        e.printStackTrace();
        log.error("Exception raised: ", e);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = e.getHttpStatus();
        String body = httpStatus.is4xxClientError() ? e.getMessage() : INTERNAL_SERVER_ERROR_MESSAGE;

        return ResponseEntity.status(httpStatus).headers(httpHeaders).body(body);
    }

    @ExceptionHandler({InputValidationException.class, ValidationException.class})
    public String handleBadRequestException(Exception e, Model model) {
        e.printStackTrace();
        log.error("Exception raised: ", e);
        model.addAttribute("errorCode", HttpStatus.BAD_REQUEST);
        model.addAttribute("message", e.getMessage());
        return "custom-error";
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class, MissingPathVariableException.class,
            MissingServletRequestParameterException.class, ServletRequestBindingException.class,
            ConversionNotSupportedException.class, TypeMismatchException.class, HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class, MethodArgumentNotValidException.class, MissingServletRequestPartException.class,
            BindException.class, NoHandlerFoundException.class, AsyncRequestTimeoutException.class,
            MethodArgumentTypeMismatchException.class})
    public String handleSpringException(Exception ex, Model model) {
        HttpStatus status = null;
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            status = HttpStatus.NOT_ACCEPTABLE;
        } else if (ex instanceof MissingPathVariableException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof MissingServletRequestParameterException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ServletRequestBindingException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ConversionNotSupportedException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof TypeMismatchException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof HttpMessageNotReadableException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof HttpMessageNotWritableException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof MethodArgumentNotValidException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof MissingServletRequestPartException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof BindException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof NoHandlerFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof AsyncRequestTimeoutException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            status = HttpStatus.BAD_REQUEST;
        }
        ex.printStackTrace();
        log.error("Exception raised: ", ex);
        model.addAttribute("errorCode", status);
        model.addAttribute("message", ex.getMessage());
        return "custom-error";
    }

}

