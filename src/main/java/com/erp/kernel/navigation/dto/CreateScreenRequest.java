package com.erp.kernel.navigation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a screen definition.
 *
 * @param screenId   the unique screen identifier
 * @param title      the screen title
 * @param description the description
 * @param module     the module
 * @param tcodeId    the associated T-code ID (optional)
 * @param screenType the screen type (e.g., LIST, DETAIL, FORM, DASHBOARD)
 */
public record CreateScreenRequest(
        @NotBlank @Size(max = 50) String screenId,
        @NotBlank @Size(max = 200) String title,
        @Size(max = 500) String description,
        @NotBlank @Size(max = 50) String module,
        Long tcodeId,
        @NotBlank @Size(max = 50) String screenType
) {
}
