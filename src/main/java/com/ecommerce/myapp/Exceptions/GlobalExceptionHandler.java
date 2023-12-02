package com.ecommerce.myapp.Exceptions;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.security.sasl.AuthenticationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Nơi bắt tất cả các lỗi throw ra
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Exception for @valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidErrorRes> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ValidErrorRes validErrorRes = new ValidErrorRes(HttpStatus.BAD_REQUEST.value(),
                errors,
                request.getDescription(false),
                LocalDateTime.now());
        return ResponseEntity.badRequest().body(validErrorRes);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiError> handleException(InsufficientAuthenticationException ex,
                                                    WebRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleException(AuthenticationException ex,
                                                    WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleException(BadCredentialsException ex,
                                                    WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleException(JwtException ex,
                                                    WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex,
                                                    WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }


//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
//    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ex.getMessage());
    }

}
