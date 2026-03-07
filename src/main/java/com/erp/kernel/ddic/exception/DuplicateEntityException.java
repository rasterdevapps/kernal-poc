package com.erp.kernel.ddic.exception;

/**
 * Thrown when an attempt is made to create a DDIC entity that already exists.
 */
public class DuplicateEntityException extends RuntimeException {

    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message describing the duplicate
     */
    public DuplicateEntityException(final String message) {
        super(message);
    }
}
