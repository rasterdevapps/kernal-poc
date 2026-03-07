package com.erp.kernel.security.exception;

/**
 * Exception thrown when a user account is locked due to security policies.
 *
 * <p>This is thrown when a user exceeds the maximum number of failed login
 * attempts or when an administrator explicitly locks an account.
 */
public class AccountLockedException extends RuntimeException {

    /**
     * Creates a new account locked exception.
     *
     * @param message the detail message
     */
    public AccountLockedException(final String message) {
        super(message);
    }
}
