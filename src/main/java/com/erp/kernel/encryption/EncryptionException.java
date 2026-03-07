package com.erp.kernel.encryption;

/**
 * Thrown when an encryption or decryption operation fails.
 */
public class EncryptionException extends RuntimeException {

    /**
     * Creates a new exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public EncryptionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
