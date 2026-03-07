package com.erp.kernel.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for obtaining a JWT token.
 *
 * @param username the username
 * @param password the password
 */
public record TokenRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
