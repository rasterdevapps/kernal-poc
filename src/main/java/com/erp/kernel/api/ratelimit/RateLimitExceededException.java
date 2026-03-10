package com.erp.kernel.api.ratelimit;

/**
 * Exception thrown when a client exceeds the configured API rate limit.
 */
public class RateLimitExceededException extends RuntimeException {

    /**
     * Creates a new rate limit exceeded exception.
     *
     * @param message the detail message
     */
    public RateLimitExceededException(final String message) {
        super(message);
    }
}
