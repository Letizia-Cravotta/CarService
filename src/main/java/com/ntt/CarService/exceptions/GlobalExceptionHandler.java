package com.ntt.CarService.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName, message);
        });

        Object invalidInput = e.getBindingResult().getTarget();
        log.warn("Validation errors: {}\n caused by invalid input: {}", response, invalidInput);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Map<String, String>> handleAPIException(APIException e) {
        log.warn("APIException: {}", e.getMessage());
        Map<String, String> body = Map.of("error", e.getMessage() != null ? e.getMessage() : "API error");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}