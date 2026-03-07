package com.erp.kernel.security.auth;

/**
 * Represents the result of an authentication attempt.
 *
 * @param authenticated whether authentication succeeded
 * @param username      the username that was authenticated
 * @param providerType  the provider type that processed the authentication
 * @param message       a human-readable status message
 * @param mfaRequired   whether MFA verification is still required
 */
public record AuthenticationResult(
        boolean authenticated,
        String username,
        AuthenticationProviderType providerType,
        String message,
        boolean mfaRequired
) {

    /**
     * Creates a successful authentication result.
     *
     * @param username     the authenticated username
     * @param providerType the provider type
     * @return a successful result
     */
    public static AuthenticationResult success(final String username,
                                               final AuthenticationProviderType providerType) {
        return new AuthenticationResult(true, username, providerType,
                "Authentication successful", false);
    }

    /**
     * Creates a successful result that requires MFA verification.
     *
     * @param username     the authenticated username
     * @param providerType the provider type
     * @return a result indicating MFA is required
     */
    public static AuthenticationResult mfaRequired(final String username,
                                                   final AuthenticationProviderType providerType) {
        return new AuthenticationResult(true, username, providerType,
                "MFA verification required", true);
    }

    /**
     * Creates a failed authentication result.
     *
     * @param username     the username that failed
     * @param providerType the provider type
     * @param message      the failure reason
     * @return a failed result
     */
    public static AuthenticationResult failure(final String username,
                                               final AuthenticationProviderType providerType,
                                               final String message) {
        return new AuthenticationResult(false, username, providerType, message, false);
    }
}
