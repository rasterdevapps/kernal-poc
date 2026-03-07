package com.erp.kernel.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new user.
 *
 * @param username    the username
 * @param password    the password (optional for LDAP users)
 * @param email       the email address
 * @param firstName   the first name
 * @param lastName    the last name
 * @param displayName the display name
 * @param ldapDn      the LDAP distinguished name (optional)
 */
public record CreateUserRequest(
        @NotBlank @Size(max = 100) String username,
        @Size(max = 255) String password,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @Size(max = 200) String displayName,
        @Size(max = 500) String ldapDn
) {
}
