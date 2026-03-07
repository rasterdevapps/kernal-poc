package com.erp.kernel.security.dto;

import java.time.Instant;

/**
 * Response DTO for login policy information.
 *
 * @param id                     the policy ID
 * @param policyName             the policy name
 * @param description            the description
 * @param minPasswordLength      the minimum password length
 * @param requireUppercase       whether uppercase is required
 * @param requireLowercase       whether lowercase is required
 * @param requireDigit           whether digits are required
 * @param requireSpecialChar     whether special characters are required
 * @param passwordExpiryDays     the password expiry in days
 * @param maxFailedAttempts      the max failed attempts before lockout
 * @param lockoutDurationMinutes the lockout duration in minutes
 * @param sessionTimeoutMinutes  the session timeout in minutes
 * @param enforceMfa             whether MFA is enforced
 * @param active                 whether the policy is active
 * @param createdAt              the creation timestamp
 * @param updatedAt              the last update timestamp
 */
public record LoginPolicyDto(
        Long id,
        String policyName,
        String description,
        int minPasswordLength,
        boolean requireUppercase,
        boolean requireLowercase,
        boolean requireDigit,
        boolean requireSpecialChar,
        int passwordExpiryDays,
        int maxFailedAttempts,
        int lockoutDurationMinutes,
        int sessionTimeoutMinutes,
        boolean enforceMfa,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
