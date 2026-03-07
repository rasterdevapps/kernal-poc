package com.erp.kernel.security.mapper;

import com.erp.kernel.security.dto.CreateRoleRequest;
import com.erp.kernel.security.dto.RoleDto;
import com.erp.kernel.security.entity.Role;

import java.util.Objects;

/**
 * Maps between {@link Role} entities and their DTOs.
 */
public final class RoleMapper {

    private RoleMapper() {
    }

    /**
     * Converts a role entity to its response DTO.
     *
     * @param entity the role entity
     * @return the role DTO
     */
    public static RoleDto toDto(final Role entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new RoleDto(
                entity.getId(),
                entity.getRoleName(),
                entity.getDescription(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new role entity.
     *
     * @param request the create request
     * @return a new role entity (not yet persisted)
     */
    public static Role toEntity(final CreateRoleRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var role = new Role();
        role.setRoleName(request.roleName());
        role.setDescription(request.description());
        return role;
    }

    /**
     * Updates an existing role entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     */
    public static void updateEntity(final Role entity, final CreateRoleRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setRoleName(request.roleName());
        entity.setDescription(request.description());
    }
}
