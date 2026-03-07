package com.erp.kernel.security.mapper;

import com.erp.kernel.security.dto.CreateRoleRequest;
import com.erp.kernel.security.entity.Role;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link RoleMapper}.
 */
class RoleMapperTest {

    @Test
    void shouldMapEntityToDto() {
        final var entity = createRole();

        final var dto = RoleMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.roleName()).isEqualTo("ADMIN");
        assertThat(dto.description()).isEqualTo("Administrator");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isNotNull();
        assertThat(dto.updatedAt()).isNotNull();
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNull() {
        assertThatThrownBy(() -> RoleMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldMapRequestToEntity() {
        final var request = new CreateRoleRequest("ADMIN", "Administrator");

        final var entity = RoleMapper.toEntity(request);

        assertThat(entity.getRoleName()).isEqualTo("ADMIN");
        assertThat(entity.getDescription()).isEqualTo("Administrator");
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNullForToEntity() {
        assertThatThrownBy(() -> RoleMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldUpdateEntity() {
        final var entity = createRole();
        final var request = new CreateRoleRequest("USER", "Basic user");

        RoleMapper.updateEntity(entity, request);

        assertThat(entity.getRoleName()).isEqualTo("USER");
        assertThat(entity.getDescription()).isEqualTo("Basic user");
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNullForUpdate() {
        final var request = new CreateRoleRequest("ROLE", "desc");
        assertThatThrownBy(() -> RoleMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNullForUpdate() {
        assertThatThrownBy(() -> RoleMapper.updateEntity(new Role(), null))
                .isInstanceOf(NullPointerException.class);
    }

    private Role createRole() {
        final var role = new Role();
        role.setId(1L);
        role.setRoleName("ADMIN");
        role.setDescription("Administrator");
        role.setActive(true);
        role.setCreatedAt(Instant.now());
        role.setUpdatedAt(Instant.now());
        return role;
    }
}
