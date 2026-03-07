package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateDomainRequest;
import com.erp.kernel.ddic.dto.DomainDto;
import com.erp.kernel.ddic.entity.Domain;

import java.util.Objects;

/**
 * Maps between {@link Domain} entities and their DTOs.
 */
public final class DomainMapper {

    private DomainMapper() {
    }

    /**
     * Converts a domain entity to its response DTO.
     *
     * @param entity the domain entity
     * @return the domain DTO
     */
    public static DomainDto toDto(final Domain entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new DomainDto(
                entity.getId(),
                entity.getDomainName(),
                entity.getDataType(),
                entity.getMaxLength(),
                entity.getDecimalPlaces(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new domain entity.
     *
     * @param request the create request
     * @return a new domain entity (not yet persisted)
     */
    public static Domain toEntity(final CreateDomainRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var domain = new Domain();
        domain.setDomainName(request.domainName());
        domain.setDataType(request.dataType());
        domain.setMaxLength(request.maxLength());
        domain.setDecimalPlaces(request.decimalPlaces());
        domain.setDescription(request.description());
        return domain;
    }

    /**
     * Updates an existing domain entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     */
    public static void updateEntity(final Domain entity, final CreateDomainRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setDomainName(request.domainName());
        entity.setDataType(request.dataType());
        entity.setMaxLength(request.maxLength());
        entity.setDecimalPlaces(request.decimalPlaces());
        entity.setDescription(request.description());
    }
}
