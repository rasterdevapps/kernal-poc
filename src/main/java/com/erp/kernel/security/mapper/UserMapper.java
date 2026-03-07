package com.erp.kernel.security.mapper;

import com.erp.kernel.security.dto.CreateUserRequest;
import com.erp.kernel.security.dto.UserDto;
import com.erp.kernel.security.entity.User;

import java.util.Objects;

/**
 * Maps between {@link User} entities and their DTOs.
 */
public final class UserMapper {

    private UserMapper() {
    }

    /**
     * Converts a user entity to its response DTO.
     *
     * @param entity the user entity
     * @return the user DTO
     */
    public static UserDto toDto(final User entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new UserDto(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDisplayName(),
                entity.isActive(),
                entity.isLocked(),
                entity.getLdapDn(),
                entity.getLastLoginAt(),
                entity.getPasswordChangedAt(),
                entity.getFailedLoginCount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new user entity.
     *
     * @param request the create request
     * @return a new user entity (not yet persisted)
     */
    public static User toEntity(final CreateUserRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setDisplayName(request.displayName());
        user.setLdapDn(request.ldapDn());
        return user;
    }

    /**
     * Updates an existing user entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     */
    public static void updateEntity(final User entity, final CreateUserRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setUsername(request.username());
        entity.setEmail(request.email());
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setDisplayName(request.displayName());
        entity.setLdapDn(request.ldapDn());
    }
}
