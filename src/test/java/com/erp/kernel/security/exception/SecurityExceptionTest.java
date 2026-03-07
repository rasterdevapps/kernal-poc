package com.erp.kernel.security.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for security exception classes.
 */
class SecurityExceptionTest {

    @Test
    void shouldCreateAuthenticationException() {
        final var ex = new AuthenticationException("auth failed");

        assertThat(ex.getMessage()).isEqualTo("auth failed");
    }

    @Test
    void shouldCreateAuthenticationExceptionWithCause() {
        final var cause = new RuntimeException("root cause");
        final var ex = new AuthenticationException("auth failed", cause);

        assertThat(ex.getMessage()).isEqualTo("auth failed");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    void shouldCreateAuthorizationException() {
        final var ex = new AuthorizationException("access denied");

        assertThat(ex.getMessage()).isEqualTo("access denied");
    }

    @Test
    void shouldCreateAccountLockedException() {
        final var ex = new AccountLockedException("account locked");

        assertThat(ex.getMessage()).isEqualTo("account locked");
    }
}
