package com.erp.kernel.security.service;

import com.erp.kernel.security.dto.CreateRoleRequest;
import com.erp.kernel.security.entity.Role;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link RoleService}.
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void shouldCreateRole_whenNameIsUnique() {
        final var request = new CreateRoleRequest("ADMIN", "Administrator role");
        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            final var saved = invocation.<Role>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = roleService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.roleName()).isEqualTo("ADMIN");
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenRoleNameExists() {
        final var request = new CreateRoleRequest("ADMIN", "desc");
        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(true);

        assertThatThrownBy(() -> roleService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("ADMIN");
        verify(roleRepository, never()).save(any());
    }

    @Test
    void shouldReturnRole_whenFoundById() {
        final var entity = createRole(1L, "ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = roleService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.roleName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenRoleNotFoundById() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(
                createRole(1L, "ADMIN"), createRole(2L, "USER")));

        final var result = roleService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateRole_whenIdExistsAndNameIsUnique() {
        final var entity = createRole(1L, "OLD_NAME");
        final var request = new CreateRoleRequest("NEW_NAME", "Updated");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(roleRepository.findByRoleName("NEW_NAME")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = roleService.update(1L, request);

        assertThat(result.roleName()).isEqualTo("NEW_NAME");
    }

    @Test
    void shouldUpdateRole_whenNameUnchanged() {
        final var entity = createRole(1L, "SAME_NAME");
        final var request = new CreateRoleRequest("SAME_NAME", "Updated");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(roleRepository.findByRoleName("SAME_NAME")).thenReturn(Optional.of(entity));
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = roleService.update(1L, request);

        assertThat(result.roleName()).isEqualTo("SAME_NAME");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentRole() {
        final var request = new CreateRoleRequest("NAME", "desc");
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateNameConflicts() {
        final var entity = createRole(1L, "ORIGINAL");
        final var conflicting = createRole(2L, "TAKEN");
        final var request = new CreateRoleRequest("TAKEN", "desc");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(roleRepository.findByRoleName("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> roleService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("TAKEN");
    }

    @Test
    void shouldDeleteRole_whenIdExists() {
        when(roleRepository.existsById(1L)).thenReturn(true);

        roleService.delete(1L);

        verify(roleRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentRole() {
        when(roleRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> roleService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Role createRole(final Long id, final String name) {
        final var role = new Role();
        role.setId(id);
        role.setRoleName(name);
        role.setDescription("Test role");
        role.setActive(true);
        role.setCreatedAt(Instant.now());
        role.setUpdatedAt(Instant.now());
        return role;
    }
}
