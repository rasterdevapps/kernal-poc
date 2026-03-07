package com.erp.kernel.security.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user authentication.
 *
 * @param username the username
 * @param password the password
 * @param totpCode the TOTP code (optional, required when 2FA is enabled)
 */
public record AuthenticationRequest(
        @NotBlank String username,
        @NotBlank String password,
        String totpCode
) {
}
