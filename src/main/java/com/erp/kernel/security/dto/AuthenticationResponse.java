package com.erp.kernel.security.dto;

/**
 * Response DTO for authentication results.
 *
 * @param authenticated whether the authentication succeeded
 * @param username      the authenticated username
 * @param message       a human-readable status message
 * @param mfaRequired   whether MFA verification is still required
 */
public record AuthenticationResponse(
        boolean authenticated,
        String username,
        String message,
        boolean mfaRequired
) {
}
