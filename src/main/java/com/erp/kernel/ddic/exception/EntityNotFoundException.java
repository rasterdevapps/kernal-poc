package com.erp.kernel.ddic.exception;

/**
 * Thrown when a requested DDIC entity cannot be found.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message describing which entity was not found
     */
    public EntityNotFoundException(final String message) {
        super(message);
    }
}
