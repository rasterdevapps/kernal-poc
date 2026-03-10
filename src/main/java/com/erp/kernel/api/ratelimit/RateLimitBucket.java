package com.erp.kernel.api.ratelimit;

/**
 * Token bucket implementation for rate limiting.
 *
 * <p>Each bucket tracks the number of available tokens and refills them
 * at a configured rate. A request consumes one token; if no tokens are
 * available, the request is rejected.
 */
public class RateLimitBucket {

    private final int capacity;
    private final double refillRatePerNano;
    private double tokens;
    private long lastRefillNanos;

    /**
     * Creates a new rate limit bucket.
     *
     * @param capacity          the maximum number of tokens (burst capacity)
     * @param requestsPerSecond the refill rate in requests per second
     */
    public RateLimitBucket(final int capacity, final int requestsPerSecond) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        if (requestsPerSecond <= 0) {
            throw new IllegalArgumentException("requestsPerSecond must be positive");
        }
        this.capacity = capacity;
        this.refillRatePerNano = requestsPerSecond / 1_000_000_000.0;
        this.tokens = capacity;
        this.lastRefillNanos = System.nanoTime();
    }

    /**
     * Attempts to consume one token from the bucket.
     *
     * @return {@code true} if a token was consumed, {@code false} if the bucket is empty
     */
    public synchronized boolean tryConsume() {
        refill();
        if (tokens >= 1.0) {
            tokens -= 1.0;
            return true;
        }
        return false;
    }

    /**
     * Returns the current number of available tokens.
     *
     * @return the available tokens (may be fractional due to refill timing)
     */
    public synchronized double getAvailableTokens() {
        refill();
        return tokens;
    }

    private void refill() {
        final var now = System.nanoTime();
        final var elapsed = now - lastRefillNanos;
        final var newTokens = elapsed * refillRatePerNano;
        tokens = Math.min(capacity, tokens + newTokens);
        lastRefillNanos = now;
    }
}
