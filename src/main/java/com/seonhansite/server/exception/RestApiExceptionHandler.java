package com.seonhansite.server.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<RestApiExceptionMessage> handleCustomException(RestApiException exception) {
        return new ResponseEntity<>(
                new RestApiExceptionMessage(exception),
                exception.getStatusCode());
    }
}
