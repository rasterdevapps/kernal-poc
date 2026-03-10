package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateNamingTemplateRequest;
import com.erp.kernel.navigation.dto.NamingTemplateDto;
import com.erp.kernel.navigation.entity.NamingTemplate;

import java.util.Objects;

/**
 * Mapper for converting between {@link NamingTemplate} entities and DTOs.
 */
public final class NamingTemplateMapper {

    private NamingTemplateMapper() {
    }

    /**
     * Converts a naming template entity to its response DTO.
     *
     * @param entity the naming template entity
     * @return the naming template DTO
     */
    public static NamingTemplateDto toDto(final NamingTemplate entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new NamingTemplateDto(
                entity.getId(),
                entity.getEntityType(),
                entity.getPattern(),
                entity.getDescription(),
                entity.getExample(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new naming template entity.
     *
     * @param request the create request
     * @return the naming template entity
     */
    public static NamingTemplate toEntity(final CreateNamingTemplateRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var template = new NamingTemplate();
        template.setEntityType(request.entityType());
        template.setPattern(request.pattern());
        template.setDescription(request.description());
        template.setExample(request.example());
        return template;
    }

    /**
     * Updates an existing naming template entity with values from the request.
     *
     * @param entity  the existing naming template entity
     * @param request the update request
     */
    public static void updateEntity(final NamingTemplate entity, final CreateNamingTemplateRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setPattern(request.pattern());
        entity.setDescription(request.description());
        entity.setExample(request.example());
    }
}
