package com.erp.kernel.security.mapper;

import com.erp.kernel.security.dto.PermissionDto;
import com.erp.kernel.security.entity.Permission;

import java.util.Objects;

/**
 * Maps between {@link Permission} entities and their DTOs.
 */
public final class PermissionMapper {

    private PermissionMapper() {
    }

    /**
     * Converts a permission entity to its response DTO.
     *
     * @param entity the permission entity
     * @return the permission DTO
     */
    public static PermissionDto toDto(final Permission entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new PermissionDto(
                entity.getId(),
                entity.getPermissionName(),
                entity.getDescription(),
                entity.getResource(),
                entity.getAction(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
