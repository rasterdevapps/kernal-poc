package com.erp.kernel.security.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link TotpService}.
 */
class TotpServiceTest {

    private final TotpService totpService = new TotpService();

    @Test
    void shouldGenerateSecret() {
        final var secret = totpService.generateSecret();

        assertThat(secret).isNotNull().isNotEmpty();
    }

    @Test
    void shouldGenerateUniqueSecrets() {
        final var secret1 = totpService.generateSecret();
        final var secret2 = totpService.generateSecret();

        assertThat(secret1).isNotEqualTo(secret2);
    }

    @Test
    void shouldGenerateProvisioningUri() {
        final var uri = totpService.generateProvisioningUri("user", "ERP", "SECRETKEY");

        assertThat(uri).startsWith("otpauth://totp/");
        assertThat(uri).contains("user");
        assertThat(uri).contains("ERP");
        assertThat(uri).contains("SECRETKEY");
    }

    @Test
    void shouldValidateCorrectCode() {
        final var secret = totpService.generateSecret();
        final var timeStep = java.time.Instant.now().getEpochSecond() / 30;
        final var code = totpService.generateCode(secret, timeStep);
        final var codeStr = String.valueOf(code);

        assertThat(totpService.validateCode(codeStr, secret)).isTrue();
    }

    @Test
    void shouldRejectInvalidCode() {
        final var secret = totpService.generateSecret();

        assertThat(totpService.validateCode("000000", secret)).isFalse();
    }

    @Test
    void shouldRejectNullCode() {
        final var secret = totpService.generateSecret();

        assertThat(totpService.validateCode(null, secret)).isFalse();
    }

    @Test
    void shouldRejectNullSecret() {
        assertThat(totpService.validateCode("123456", null)).isFalse();
    }

    @Test
    void shouldRejectNonNumericCode() {
        final var secret = totpService.generateSecret();

        assertThat(totpService.validateCode("abcdef", secret)).isFalse();
    }

    @Test
    void shouldGenerateCodeForTimeStep() {
        final var secret = totpService.generateSecret();
        final var code = totpService.generateCode(secret, 12345L);

        assertThat(code).isGreaterThanOrEqualTo(0);
        assertThat(code).isLessThan(1000000);
    }

    @Test
    void shouldThrowIllegalStateException_whenHmacAlgorithmNotAvailable() {
        try (var macMock = org.mockito.Mockito.mockStatic(javax.crypto.Mac.class)) {
            macMock.when(() -> javax.crypto.Mac.getInstance("HmacSHA1"))
                    .thenThrow(new java.security.NoSuchAlgorithmException("mocked"));

            org.assertj.core.api.Assertions.assertThatThrownBy(
                    () -> totpService.generateCode("dGVzdHNlY3JldA", 1L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("TOTP generation failed");
        }
    }
}
