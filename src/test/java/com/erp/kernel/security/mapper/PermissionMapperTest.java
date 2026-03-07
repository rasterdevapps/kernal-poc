package com.erp.kernel.security.mapper;

import com.erp.kernel.security.entity.Permission;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link PermissionMapper}.
 */
class PermissionMapperTest {

    @Test
    void shouldMapEntityToDto() {
        final var entity = new Permission();
        entity.setId(1L);
        entity.setPermissionName("USER_READ");
        entity.setDescription("Read user data");
        entity.setResource("users");
        entity.setAction("READ");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        final var dto = PermissionMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.permissionName()).isEqualTo("USER_READ");
        assertThat(dto.description()).isEqualTo("Read user data");
        assertThat(dto.resource()).isEqualTo("users");
        assertThat(dto.action()).isEqualTo("READ");
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNull() {
        assertThatThrownBy(() -> PermissionMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }
}
