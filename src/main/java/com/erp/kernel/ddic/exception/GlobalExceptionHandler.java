package com.erp.kernel.ddic.exception;

import com.erp.kernel.security.exception.AccountLockedException;
import com.erp.kernel.security.exception.AuthenticationException;
import com.erp.kernel.security.exception.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler that maps domain exceptions to HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles entity not found exceptions.
     *
     * @param ex the exception
     * @return a 404 response with the error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final EntityNotFoundException ex) {
        LOG.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Handles duplicate entity exceptions.
     *
     * @param ex the exception
     * @return a 409 response with the error message
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(final DuplicateEntityException ex) {
        LOG.warn("Duplicate entity: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Handles validation exceptions from Jakarta Bean Validation.
     *
     * @param ex the exception
     * @return a 400 response with field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex) {
        final var message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        LOG.warn("Validation failed: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    /**
     * Handles business validation exceptions.
     *
     * @param ex the exception
     * @return a 400 response with the error message
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(final ValidationException ex) {
        LOG.warn("Business validation failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Handles authentication exceptions.
     *
     * @param ex the exception
     * @return a 401 response with the error message
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(final AuthenticationException ex) {
        LOG.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Handles authorisation exceptions.
     *
     * @param ex the exception
     * @return a 403 response with the error message
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorization(final AuthorizationException ex) {
        LOG.warn("Authorisation denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Handles account locked exceptions.
     *
     * @param ex the exception
     * @return a 423 response with the error message
     */
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountLocked(final AccountLockedException ex) {
        LOG.warn("Account locked: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(new ErrorResponse(ex.getMessage()));
    }
}
