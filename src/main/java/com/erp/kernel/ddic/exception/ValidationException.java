package com.erp.kernel.ddic.exception;

/**
 * Thrown when a business validation rule is violated in the DDIC layer.
 */
public class ValidationException extends RuntimeException {

    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message describing the validation failure
     */
    public ValidationException(final String message) {
        super(message);
    }
}
