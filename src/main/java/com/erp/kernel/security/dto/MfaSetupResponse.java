package com.erp.kernel.security.dto;

/**
 * Response DTO for MFA setup containing the secret and provisioning URI.
 *
 * @param secretKey      the Base32-encoded TOTP secret key
 * @param provisioningUri the otpauth:// URI for QR code generation
 */
public record MfaSetupResponse(
        String secretKey,
        String provisioningUri
) {
}
