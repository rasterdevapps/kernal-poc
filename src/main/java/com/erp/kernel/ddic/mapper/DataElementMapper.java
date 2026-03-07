package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateDataElementRequest;
import com.erp.kernel.ddic.dto.DataElementDto;
import com.erp.kernel.ddic.entity.DataElement;
import com.erp.kernel.ddic.entity.Domain;

import java.util.Objects;

/**
 * Maps between {@link DataElement} entities and their DTOs.
 */
public final class DataElementMapper {

    private DataElementMapper() {
    }

    /**
     * Converts a data element entity to its response DTO.
     *
     * @param entity the data element entity
     * @return the data element DTO
     */
    public static DataElementDto toDto(final DataElement entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new DataElementDto(
                entity.getId(),
                entity.getElementName(),
                entity.getDomain().getId(),
                entity.getDomain().getDomainName(),
                entity.getShortLabel(),
                entity.getMediumLabel(),
                entity.getLongLabel(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new data element entity.
     *
     * @param request the create request
     * @param domain  the referenced domain
     * @return a new data element entity (not yet persisted)
     */
    public static DataElement toEntity(final CreateDataElementRequest request, final Domain domain) {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(domain, "domain must not be null");
        final var element = new DataElement();
        element.setElementName(request.elementName());
        element.setDomain(domain);
        element.setShortLabel(request.shortLabel());
        element.setMediumLabel(request.mediumLabel());
        element.setLongLabel(request.longLabel());
        element.setDescription(request.description());
        return element;
    }

    /**
     * Updates an existing data element entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     * @param domain  the referenced domain
     */
    public static void updateEntity(final DataElement entity,
                                    final CreateDataElementRequest request,
                                    final Domain domain) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(domain, "domain must not be null");
        entity.setElementName(request.elementName());
        entity.setDomain(domain);
        entity.setShortLabel(request.shortLabel());
        entity.setMediumLabel(request.mediumLabel());
        entity.setLongLabel(request.longLabel());
        entity.setDescription(request.description());
    }
}
