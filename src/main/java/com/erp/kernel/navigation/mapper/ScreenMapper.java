package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateScreenRequest;
import com.erp.kernel.navigation.dto.ScreenDto;
import com.erp.kernel.navigation.entity.Screen;

import java.util.Objects;

/**
 * Mapper for converting between {@link Screen} entities and DTOs.
 */
public final class ScreenMapper {

    private ScreenMapper() {
    }

    /**
     * Converts a screen entity to its response DTO.
     *
     * @param entity the screen entity
     * @return the screen DTO
     */
    public static ScreenDto toDto(final Screen entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new ScreenDto(
                entity.getId(),
                entity.getScreenId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getModule(),
                entity.getTcodeId(),
                entity.getScreenType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new screen entity.
     *
     * @param request the create request
     * @return the screen entity
     */
    public static Screen toEntity(final CreateScreenRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var screen = new Screen();
        screen.setScreenId(request.screenId());
        screen.setTitle(request.title());
        screen.setDescription(request.description());
        screen.setModule(request.module());
        screen.setTcodeId(request.tcodeId());
        screen.setScreenType(request.screenType());
        return screen;
    }

    /**
     * Updates an existing screen entity with values from the request.
     *
     * @param entity  the existing screen entity
     * @param request the update request
     */
    public static void updateEntity(final Screen entity, final CreateScreenRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setModule(request.module());
        entity.setTcodeId(request.tcodeId());
        entity.setScreenType(request.screenType());
    }
}
