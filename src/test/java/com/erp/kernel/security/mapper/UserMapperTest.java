package com.erp.kernel.security.mapper;

import com.erp.kernel.security.dto.CreateUserRequest;
import com.erp.kernel.security.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link UserMapper}.
 */
class UserMapperTest {

    @Test
    void shouldMapEntityToDto() {
        final var entity = createUser();

        final var dto = UserMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.username()).isEqualTo("jdoe");
        assertThat(dto.email()).isEqualTo("jdoe@erp.com");
        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.displayName()).isEqualTo("John Doe");
        assertThat(dto.active()).isTrue();
        assertThat(dto.locked()).isFalse();
        assertThat(dto.ldapDn()).isEqualTo("CN=jdoe");
        assertThat(dto.lastLoginAt()).isNotNull();
        assertThat(dto.passwordChangedAt()).isNotNull();
        assertThat(dto.failedLoginCount()).isEqualTo(2);
        assertThat(dto.createdAt()).isNotNull();
        assertThat(dto.updatedAt()).isNotNull();
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNull() {
        assertThatThrownBy(() -> UserMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldMapRequestToEntity() {
        final var request = new CreateUserRequest("jdoe", "pass", "jdoe@erp.com",
                "John", "Doe", "John Doe", "CN=jdoe");

        final var entity = UserMapper.toEntity(request);

        assertThat(entity.getUsername()).isEqualTo("jdoe");
        assertThat(entity.getEmail()).isEqualTo("jdoe@erp.com");
        assertThat(entity.getFirstName()).isEqualTo("John");
        assertThat(entity.getLastName()).isEqualTo("Doe");
        assertThat(entity.getDisplayName()).isEqualTo("John Doe");
        assertThat(entity.getLdapDn()).isEqualTo("CN=jdoe");
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNullForToEntity() {
        assertThatThrownBy(() -> UserMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldUpdateEntity() {
        final var entity = createUser();
        final var request = new CreateUserRequest("newname", null, "new@erp.com",
                "New", "Name", "New Name", null);

        UserMapper.updateEntity(entity, request);

        assertThat(entity.getUsername()).isEqualTo("newname");
        assertThat(entity.getEmail()).isEqualTo("new@erp.com");
        assertThat(entity.getFirstName()).isEqualTo("New");
        assertThat(entity.getLastName()).isEqualTo("Name");
        assertThat(entity.getDisplayName()).isEqualTo("New Name");
        assertThat(entity.getLdapDn()).isNull();
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNullForUpdate() {
        final var request = new CreateUserRequest("n", null, "e@e.com", "F", "L", null, null);
        assertThatThrownBy(() -> UserMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNullForUpdate() {
        assertThatThrownBy(() -> UserMapper.updateEntity(new User(), null))
                .isInstanceOf(NullPointerException.class);
    }

    private User createUser() {
        final var user = new User();
        user.setId(1L);
        user.setUsername("jdoe");
        user.setEmail("jdoe@erp.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDisplayName("John Doe");
        user.setActive(true);
        user.setLocked(false);
        user.setLdapDn("CN=jdoe");
        user.setLastLoginAt(Instant.now());
        user.setPasswordChangedAt(Instant.now());
        user.setFailedLoginCount(2);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return user;
    }
}
