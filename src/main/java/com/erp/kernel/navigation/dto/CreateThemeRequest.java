package com.erp.kernel.navigation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new theme.
 *
 * @param themeName      the theme name
 * @param description    the description
 * @param primaryColor   the primary colour as a hex code (e.g., #FF5733)
 * @param secondaryColor the secondary colour as a hex code
 * @param isDefault      whether this is the default theme
 */
public record CreateThemeRequest(
        @NotBlank @Size(max = 100) String themeName,
        @Size(max = 500) String description,
        @NotBlank @Pattern(regexp = "^#[0-9A-Fa-f]{6}$") String primaryColor,
        @NotBlank @Pattern(regexp = "^#[0-9A-Fa-f]{6}$") String secondaryColor,
        boolean isDefault
) {
}
