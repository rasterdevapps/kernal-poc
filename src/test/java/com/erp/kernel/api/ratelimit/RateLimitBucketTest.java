package com.erp.kernel.api.ratelimit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link RateLimitBucket}.
 */
class RateLimitBucketTest {

    @Test
    void shouldThrowException_whenCapacityIsZero() {
        assertThatThrownBy(() -> new RateLimitBucket(0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("capacity must be positive");
    }

    @Test
    void shouldThrowException_whenCapacityIsNegative() {
        assertThatThrownBy(() -> new RateLimitBucket(-1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("capacity must be positive");
    }

    @Test
    void shouldThrowException_whenRequestsPerSecondIsZero() {
        assertThatThrownBy(() -> new RateLimitBucket(10, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("requestsPerSecond must be positive");
    }

    @Test
    void shouldThrowException_whenRequestsPerSecondIsNegative() {
        assertThatThrownBy(() -> new RateLimitBucket(10, -5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("requestsPerSecond must be positive");
    }

    @Test
    void shouldReturnTrue_whenTokensAreAvailable() {
        // Arrange
        final var bucket = new RateLimitBucket(10, 5);

        // Act
        final var result = bucket.tryConsume();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalse_whenAllTokensExhausted() {
        // Arrange
        final var bucket = new RateLimitBucket(2, 1);

        // Act - consume all tokens
        assertThat(bucket.tryConsume()).isTrue();
        assertThat(bucket.tryConsume()).isTrue();
        final var result = bucket.tryConsume();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnCapacity_whenBucketIsNew() {
        // Arrange
        final var bucket = new RateLimitBucket(5, 10);

        // Act
        final var tokens = bucket.getAvailableTokens();

        // Assert
        assertThat(tokens).isGreaterThanOrEqualTo(5.0);
    }

    @Test
    void shouldDecreaseTokens_afterConsumption() {
        // Arrange
        final var bucket = new RateLimitBucket(10, 1);

        // Act
        bucket.tryConsume();
        final var tokens = bucket.getAvailableTokens();

        // Assert
        assertThat(tokens).isLessThan(10.0);
    }

    @Test
    void shouldNotExceedCapacity_whenRefilling() {
        // Arrange
        final var bucket = new RateLimitBucket(5, 1_000_000);

        // Act - tokens refill but should not exceed capacity
        final var tokens = bucket.getAvailableTokens();

        // Assert
        assertThat(tokens).isLessThanOrEqualTo(5.0);
    }

    @Test
    void shouldConsumeAllTokens_whenCapacityIsOne() {
        // Arrange
        final var bucket = new RateLimitBucket(1, 1);

        // Act
        final var first = bucket.tryConsume();
        final var second = bucket.tryConsume();

        // Assert
        assertThat(first).isTrue();
        assertThat(second).isFalse();
    }
}
