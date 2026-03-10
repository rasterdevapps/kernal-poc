package com.erp.kernel.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link TokenResponse} record.
 */
class TokenResponseTest {

    @Test
    void shouldStoreAllFields() {
        final var response = new TokenResponse("jwt-token", 3600, "Bearer");

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.expiresIn()).isEqualTo(3600);
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldCreateBearerResponse() {
        final var response = TokenResponse.bearer("my-token", 1800);

        assertThat(response.token()).isEqualTo("my-token");
        assertThat(response.expiresIn()).isEqualTo(1800);
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldImplementEquals() {
        final var resp1 = new TokenResponse("token", 3600, "Bearer");
        final var resp2 = new TokenResponse("token", 3600, "Bearer");
        final var resp3 = new TokenResponse("other", 3600, "Bearer");

        assertThat(resp1).isEqualTo(resp2);
        assertThat(resp1).isNotEqualTo(resp3);
    }

    @Test
    void shouldImplementHashCode() {
        final var resp1 = new TokenResponse("token", 3600, "Bearer");
        final var resp2 = new TokenResponse("token", 3600, "Bearer");

        assertThat(resp1.hashCode()).isEqualTo(resp2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        final var response = new TokenResponse("jwt-token", 3600, "Bearer");

        assertThat(response.toString()).contains("jwt-token", "3600", "Bearer");
    }

    @Test
    void shouldAllowCustomTokenType() {
        final var response = new TokenResponse("token", 3600, "MAC");

        assertThat(response.tokenType()).isEqualTo("MAC");
    }
}
