package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.exceptions.RestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model){
        ex.printStackTrace();
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity handleRestException(RestException ex){
        ex.printStackTrace();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String body = "{msg:My exception}";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(ex.getMessage());
    }

}
