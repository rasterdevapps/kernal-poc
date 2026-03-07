package com.erp.kernel.security.exception;

/**
 * Exception thrown when a user lacks the required authorisation.
 *
 * <p>This covers access denied scenarios where a user is authenticated
 * but does not have the necessary permissions or roles.
 */
public class AuthorizationException extends RuntimeException {

    /**
     * Creates a new authorisation exception.
     *
     * @param message the detail message
     */
    public AuthorizationException(final String message) {
        super(message);
    }
}
