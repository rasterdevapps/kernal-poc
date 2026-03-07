package com.erp.kernel.security.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new login policy.
 *
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
 */
public record CreateLoginPolicyRequest(
        @NotBlank @Size(max = 100) String policyName,
        @Size(max = 500) String description,
        @Min(1) int minPasswordLength,
        boolean requireUppercase,
        boolean requireLowercase,
        boolean requireDigit,
        boolean requireSpecialChar,
        @Min(1) int passwordExpiryDays,
        @Min(1) int maxFailedAttempts,
        @Min(1) int lockoutDurationMinutes,
        @Min(1) int sessionTimeoutMinutes,
        boolean enforceMfa
) {
}
