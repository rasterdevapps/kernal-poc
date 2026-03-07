package com.erp.kernel.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new role.
 *
 * @param roleName    the role name
 * @param description the role description
 */
public record CreateRoleRequest(
        @NotBlank @Size(max = 100) String roleName,
        @Size(max = 500) String description
) {
}
