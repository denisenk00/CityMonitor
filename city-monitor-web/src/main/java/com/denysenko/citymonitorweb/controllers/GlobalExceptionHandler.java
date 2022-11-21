package com.denysenko.citymonitorweb.controllers;

import org.springframework.http.HttpStatus;
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

}
