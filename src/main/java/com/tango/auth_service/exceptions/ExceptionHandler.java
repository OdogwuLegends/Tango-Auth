package com.tango.auth_service.exceptions;

import com.tango.auth_service.utils.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> exceptionHandler(Exception exception){
        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Validation failed")
                .status(false)
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("| "));

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Validation failed")
                .status(false)
                .error(errorMessage)
                .build();

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKeyException(DuplicateKeyException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        // Check if the cause is a ConstraintViolationException for unique constraint violations
        Throwable cause = ex.getCause();
        String errorMessage;
        if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
            errorMessage = "Duplicate entry:: a record with the given unique field already exists.";
        } else {
            errorMessage = ex.getMessage();
        }

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(errorMessage)
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = UnsupportedEncodingException.class)
    public ResponseEntity<?> handleUnsupportedEncodingException(UnsupportedEncodingException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<?> handleMessagingException(MessagingException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}


