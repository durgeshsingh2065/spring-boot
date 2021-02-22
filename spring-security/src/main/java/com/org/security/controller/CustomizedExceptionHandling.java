package com.org.security.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.org.security.response.ExceptionResponse;

@ControllerAdvice
public class CustomizedExceptionHandling {

	@ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleExceptions(Exception exception) {
        ExceptionResponse response = new ExceptionResponse();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
        response.setDate(formatter.format(new Date()));
        response.setMessage(exception.getMessage());
        ResponseEntity<Object> entity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return entity;
    }
}
