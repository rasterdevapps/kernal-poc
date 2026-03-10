package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateUserPreferenceRequest;
import com.erp.kernel.navigation.dto.UserPreferenceDto;
import com.erp.kernel.navigation.entity.UserPreference;

import java.util.Objects;

/**
 * Mapper for converting between {@link UserPreference} entities and DTOs.
 */
public final class UserPreferenceMapper {

    private UserPreferenceMapper() {
    }

    /**
     * Converts a user preference entity to its response DTO.
     *
     * @param entity the user preference entity
     * @return the user preference DTO
     */
    public static UserPreferenceDto toDto(final UserPreference entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new UserPreferenceDto(
                entity.getId(),
                entity.getUserId(),
                entity.getThemeId(),
                entity.getLocale(),
                entity.getDateFormat(),
                entity.getTimeFormat(),
                entity.getItemsPerPage(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new user preference entity.
     *
     * @param request the create request
     * @return the user preference entity
     */
    public static UserPreference toEntity(final CreateUserPreferenceRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var preference = new UserPreference();
        preference.setUserId(request.userId());
        preference.setThemeId(request.themeId());
        preference.setLocale(request.locale());
        preference.setDateFormat(request.dateFormat());
        preference.setTimeFormat(request.timeFormat());
        preference.setItemsPerPage(request.itemsPerPage());
        return preference;
    }

    /**
     * Updates an existing user preference entity with values from the request.
     *
     * @param entity  the existing user preference entity
     * @param request the update request
     */
    public static void updateEntity(final UserPreference entity, final CreateUserPreferenceRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setThemeId(request.themeId());
        entity.setLocale(request.locale());
        entity.setDateFormat(request.dateFormat());
        entity.setTimeFormat(request.timeFormat());
        entity.setItemsPerPage(request.itemsPerPage());
    }
}
