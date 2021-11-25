package com.example.Inversiones.config;

import com.example.Inversiones.exception.Exceptions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class StatusExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exceptions.class})
    protected ResponseEntity<Object> StatusException(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getCause().getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
