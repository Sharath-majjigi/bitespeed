package com.bitespeed.assignment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ContactException.class)
    ResponseEntity<ErrorResponse> handleContactExceptions(
            ContactException apiException, HttpServletRequest request) {
        return ResponseEntity.status(apiException.getStatus())
                .body(ErrorResponse.builder()
                        .message(apiException.getMsg())
                        .status(apiException.getStatus().name())
                        .path(request.getServletPath())
                        .timeStamp(LocalDate.now().toString())
                        .build());
    }
}
