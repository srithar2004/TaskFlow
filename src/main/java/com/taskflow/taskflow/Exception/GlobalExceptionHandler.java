package com.taskflow.taskflow.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST; // default

        String message = ex.getMessage();

        if (message != null) {
            if (message.toLowerCase().contains("not found")) {
                status = HttpStatus.NOT_FOUND;        // 404
            } else if (message.toLowerCase().contains("not authorized")) {
                status = HttpStatus.FORBIDDEN;         // 403
            } else if (message.toLowerCase().contains("already")) {
                status = HttpStatus.CONFLICT;          // 409 - better than 400 for duplicates
            }
        }

        ErrorResponse error = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(error, status);
    }
}