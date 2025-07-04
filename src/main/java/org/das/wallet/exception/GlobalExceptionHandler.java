package org.das.wallet.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleWalletNotFoundException(WalletNotFoundException ex) {
        log.error("Handle handleWalletNotFoundException");
        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "WALLET_NOT_FOUND",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorMessageResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        log.error("Handle InsufficientFundsException");
        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "INSUFFICIENT_FUNDS",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidOperationTypeException.class)
    public ResponseEntity<ErrorMessageResponse> handleInvalidOperationTypeException(InvalidOperationTypeException ex) {
        ErrorMessageResponse description = new ErrorMessageResponse(
                "INVALID_OPERATION_TYPE",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(description);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String description = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "CONSTRAINT_VIOLATION",
                description,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Handle MethodArgumentNotValidException");
        String description = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "VALIDATION_ERROR",
                description,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(MethodArgumentTypeMismatchException ex) {
        log.error("Handle MethodArgumentTypeMismatchException={}", ex.getMessage());
        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "VALIDATION_QUERY_ARGUMENT",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> NoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        log.error("Handle NoResourceFoundException={}", ex.getMessage());
        Map<String, Object> description = new LinkedHashMap<>();
        description.put("timestamp", LocalDateTime.now());
        description.put("path", request.getRequestURI());
        description.put("status", HttpStatus.NOT_FOUND);
        description.put("error", "Not Found");
        description.put("message", ex.getMessage());

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "VALIDATION_NoResource_Error",
                description.toString(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorMessageResponse> handleBindException(BindException ex) {
        log.error("Handle BindException");
        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "VALIDATION_ERROR",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex
    ) {
        log.error("Handle HttpMessageNotReadableException");
        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "INVALID_JSON",
                ex.getMessage(),
                LocalDateTime.now()
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Handle IllegalArgumentException");
        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "INVALID_ARGUMENT",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGenericException(Exception ex) {
        log.error("Handle Exception");
        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "INTERNAL_ERROR",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}