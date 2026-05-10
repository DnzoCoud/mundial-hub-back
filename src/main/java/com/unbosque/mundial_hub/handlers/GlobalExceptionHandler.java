package com.unbosque.mundial_hub.handlers;

import com.unbosque.mundial_hub.exceptions.AlreadyExistsException;
import com.unbosque.mundial_hub.exceptions.BadAuthenticationException;
import com.unbosque.mundial_hub.exceptions.DomainException;
import com.unbosque.mundial_hub.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(DomainException ex) {
        var err = ApiError.builder()
                .code("VALIDATION_ERROR")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(err));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        var err = ApiError.builder()
                .code("NOT_FOUND")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(err));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyExistsException(AlreadyExistsException ex) {
        var err = ApiError.builder()
                .code("ALREADY_EXISTS")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(err));
    }

    @ExceptionHandler(BadAuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadAuthenticationException(BadAuthenticationException ex) {
        var err = ApiError.builder()
                .code("BAD_AUTHENTICATION")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(err));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        var err = ApiError.builder()
                .code("INTERNAL_ERROR")
                .message("Unexpected error")
                .details(List.of(ex.getClass().getSimpleName() + ": " + safeMsg(ex.getMessage())))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(err));
    }

    private String safeMsg(String msg) {
        return msg == null ? "" : msg.replaceAll("[\\r\\n\\t]", " ").trim();
    }
}
