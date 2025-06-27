package exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleWalletNotFoundException(WalletNotFoundException ex) {
        log.error("Handle handleWalletNotFoundException");
        ErrorMessageResponse error = new ErrorMessageResponse(
                "WALLET_NOT_FOUND",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorMessageResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        log.error("Handle InsufficientFundsException");
        ErrorMessageResponse error = new ErrorMessageResponse(
                "INSUFFICIENT_FUNDS",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Handle MethodArgumentNotValidException");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorMessageResponse error = new ErrorMessageResponse(
                "VALIDATION_ERROR",
                errors.toString(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorMessageResponse> handleBindException(BindException ex) {
        log.error("Handle BindException");
        ErrorMessageResponse error = new ErrorMessageResponse(
                "VALIDATION_ERROR",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex
    ) {
        log.error("Handle HttpMessageNotReadableException");
        ErrorMessageResponse error = new ErrorMessageResponse(
                "INVALID_JSON",
                ex.getMessage(),
                LocalDateTime.now()
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Handle IllegalArgumentException");
        ErrorMessageResponse error = new ErrorMessageResponse(
                "INVALID_ARGUMENT",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGenericException(Exception ex) {
        log.error("Handle Exception");
        ErrorMessageResponse error = new ErrorMessageResponse(
                "INTERNAL_ERROR",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}