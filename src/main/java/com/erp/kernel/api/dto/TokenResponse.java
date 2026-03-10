package com.erp.kernel.api.dto;

/**
 * Response DTO containing the issued JWT token.
 *
 * @param token     the JWT token string
 * @param expiresIn the token lifetime in seconds
 * @param tokenType the token type (always "Bearer")
 */
public record TokenResponse(
        String token,
        long expiresIn,
        String tokenType
) {

    /** Default token type. */
    private static final String DEFAULT_TOKEN_TYPE = "Bearer";

    /**
     * Creates a token response with the default token type.
     *
     * @param token     the JWT token
     * @param expiresIn the expiration in seconds
     * @return the token response
     */
    public static TokenResponse bearer(final String token, final long expiresIn) {
        return new TokenResponse(token, expiresIn, DEFAULT_TOKEN_TYPE);
    }
}
