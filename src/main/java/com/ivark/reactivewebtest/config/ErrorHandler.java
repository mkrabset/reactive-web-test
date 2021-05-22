package com.ivark.reactivewebtest.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handle(HttpServletRequest request, RuntimeException ex) {
        System.out.println(request);
        return ResponseEntity.status(500).body("shit! : "+ex);
    }


}
