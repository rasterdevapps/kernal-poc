package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateTCodeRequest;
import com.erp.kernel.navigation.dto.TCodeDto;
import com.erp.kernel.navigation.entity.TCode;

import java.util.Objects;

/**
 * Mapper for converting between {@link TCode} entities and DTOs.
 */
public final class TCodeMapper {

    private TCodeMapper() {
    }

    /**
     * Converts a T-code entity to its response DTO.
     *
     * @param entity the T-code entity
     * @return the T-code DTO
     */
    public static TCodeDto toDto(final TCode entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new TCodeDto(
                entity.getId(),
                entity.getCode(),
                entity.getDescription(),
                entity.getModule(),
                entity.getRoute(),
                entity.getIcon(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new T-code entity.
     *
     * @param request the create request
     * @return the T-code entity
     */
    public static TCode toEntity(final CreateTCodeRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var tcode = new TCode();
        tcode.setCode(request.code());
        tcode.setDescription(request.description());
        tcode.setModule(request.module());
        tcode.setRoute(request.route());
        tcode.setIcon(request.icon());
        return tcode;
    }

    /**
     * Updates an existing T-code entity with values from the request.
     *
     * @param entity  the existing T-code entity
     * @param request the update request
     */
    public static void updateEntity(final TCode entity, final CreateTCodeRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setDescription(request.description());
        entity.setModule(request.module());
        entity.setRoute(request.route());
        entity.setIcon(request.icon());
    }
}
