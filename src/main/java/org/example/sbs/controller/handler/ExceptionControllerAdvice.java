package org.example.sbs.controller.handler;

import org.example.sbs.exception.ForbiddenException;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.exception.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String name = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(name, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> notFoundExceptionHandler(NotFoundException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("errorCode", "404");
        response.put("errorMessage", exception.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> unauthorizedExceptionHandler(UnauthorizedException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("errorCode", "401");
        response.put("errorMessage", exception.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("errorCode", "400");
        response.put("errorMessage", exception.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> dataIntegrityViolationExceptionHandler(ForbiddenException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("errorCode", "403");
        response.put("errorMessage", exception.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
