package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateThemeRequest;
import com.erp.kernel.navigation.dto.ThemeDto;
import com.erp.kernel.navigation.entity.Theme;

import java.util.Objects;

/**
 * Mapper for converting between {@link Theme} entities and DTOs.
 */
public final class ThemeMapper {

    private ThemeMapper() {
    }

    /**
     * Converts a theme entity to its response DTO.
     *
     * @param entity the theme entity
     * @return the theme DTO
     */
    public static ThemeDto toDto(final Theme entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new ThemeDto(
                entity.getId(),
                entity.getThemeName(),
                entity.getDescription(),
                entity.getPrimaryColor(),
                entity.getSecondaryColor(),
                entity.isActive(),
                entity.isDefault(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new theme entity.
     *
     * @param request the create request
     * @return the theme entity
     */
    public static Theme toEntity(final CreateThemeRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var theme = new Theme();
        theme.setThemeName(request.themeName());
        theme.setDescription(request.description());
        theme.setPrimaryColor(request.primaryColor());
        theme.setSecondaryColor(request.secondaryColor());
        theme.setDefault(request.isDefault());
        return theme;
    }

    /**
     * Updates an existing theme entity with values from the request.
     *
     * @param entity  the existing theme entity
     * @param request the update request
     */
    public static void updateEntity(final Theme entity, final CreateThemeRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setDescription(request.description());
        entity.setPrimaryColor(request.primaryColor());
        entity.setSecondaryColor(request.secondaryColor());
        entity.setDefault(request.isDefault());
    }
}
