package com.erp.kernel.navigation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new T-code.
 *
 * @param code        the T-code (e.g., SU01, SE11)
 * @param description the description
 * @param module      the module (e.g., SECURITY, DDIC, ADMIN)
 * @param route       the navigation route
 * @param icon        the icon identifier (optional)
 */
public record CreateTCodeRequest(
        @NotBlank @Size(max = 20) String code,
        @NotBlank @Size(max = 500) String description,
        @NotBlank @Size(max = 50) String module,
        @NotBlank @Size(max = 500) String route,
        @Size(max = 100) String icon
) {
}
