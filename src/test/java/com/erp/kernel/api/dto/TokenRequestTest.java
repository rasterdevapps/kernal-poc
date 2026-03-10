package com.erp.kernel.api.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link TokenRequest} record.
 */
class TokenRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldStoreUsernameAndPassword() {
        final var request = new TokenRequest("admin", "secret");

        assertThat(request.username()).isEqualTo("admin");
        assertThat(request.password()).isEqualTo("secret");
    }

    @Test
    void shouldFailValidation_whenUsernameIsBlank() {
        final var request = new TokenRequest("", "password");

        final var violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    @Test
    void shouldFailValidation_whenPasswordIsBlank() {
        final var request = new TokenRequest("admin", "");

        final var violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldFailValidation_whenUsernameIsNull() {
        final var request = new TokenRequest(null, "password");

        final var violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void shouldFailValidation_whenPasswordIsNull() {
        final var request = new TokenRequest("admin", null);

        final var violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void shouldPassValidation_whenBothFieldsAreValid() {
        final var request = new TokenRequest("admin", "password123");

        final var violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldImplementEquals() {
        final var req1 = new TokenRequest("user", "pass");
        final var req2 = new TokenRequest("user", "pass");
        final var req3 = new TokenRequest("other", "pass");

        assertThat(req1).isEqualTo(req2);
        assertThat(req1).isNotEqualTo(req3);
    }

    @Test
    void shouldImplementHashCode() {
        final var req1 = new TokenRequest("user", "pass");
        final var req2 = new TokenRequest("user", "pass");

        assertThat(req1.hashCode()).isEqualTo(req2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        final var request = new TokenRequest("admin", "secret");

        assertThat(request.toString()).contains("admin", "secret");
    }
}
