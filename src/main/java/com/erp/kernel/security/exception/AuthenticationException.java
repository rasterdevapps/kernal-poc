package com.erp.kernel.security.exception;

/**
 * Exception thrown when authentication fails.
 *
 * <p>This covers invalid credentials, expired passwords, and other
 * authentication failures that prevent a user from logging in.
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Creates a new authentication exception.
     *
     * @param message the detail message
     */
    public AuthenticationException(final String message) {
        super(message);
    }

    /**
     * Creates a new authentication exception with a cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
