package com.erp.kernel.security.dto;

import java.time.Instant;

/**
 * Response DTO for user information.
 *
 * @param id               the user ID
 * @param username         the username
 * @param email            the email address
 * @param firstName        the first name
 * @param lastName         the last name
 * @param displayName      the display name
 * @param active           whether the user is active
 * @param locked           whether the user is locked
 * @param ldapDn           the LDAP distinguished name
 * @param lastLoginAt      the last login timestamp
 * @param passwordChangedAt the password last changed timestamp
 * @param failedLoginCount the failed login attempt count
 * @param createdAt        the creation timestamp
 * @param updatedAt        the last update timestamp
 */
public record UserDto(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String displayName,
        boolean active,
        boolean locked,
        String ldapDn,
        Instant lastLoginAt,
        Instant passwordChangedAt,
        int failedLoginCount,
        Instant createdAt,
        Instant updatedAt
) {
}
