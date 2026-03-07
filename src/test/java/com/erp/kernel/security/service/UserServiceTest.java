package com.erp.kernel.security.service;

import com.erp.kernel.security.dto.CreateUserRequest;
import com.erp.kernel.security.entity.User;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.repository.UserRepository;
import com.erp.kernel.security.repository.UserRoleRepository;
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
 * Tests for the {@link UserService}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser_whenUsernameAndEmailAreUnique() {
        final var request = new CreateUserRequest("jdoe", "Pass123!", "jdoe@erp.com",
                "John", "Doe", "John Doe", null);
        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(userRepository.existsByEmail("jdoe@erp.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            final var saved = invocation.<User>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = userService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("jdoe");
        assertThat(result.email()).isEqualTo("jdoe@erp.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldCreateUser_whenPasswordIsNull() {
        final var request = new CreateUserRequest("ldapuser", null, "ldap@erp.com",
                "Ldap", "User", "Ldap User", "CN=ldapuser,DC=erp");
        when(userRepository.existsByUsername("ldapuser")).thenReturn(false);
        when(userRepository.existsByEmail("ldap@erp.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            final var saved = invocation.<User>getArgument(0);
            saved.setId(2L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = userService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("ldapuser");
    }

    @Test
    void shouldCreateUser_whenPasswordIsBlank() {
        final var request = new CreateUserRequest("blankpw", "  ", "blank@erp.com",
                "Blank", "PW", null, null);
        when(userRepository.existsByUsername("blankpw")).thenReturn(false);
        when(userRepository.existsByEmail("blank@erp.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            final var saved = invocation.<User>getArgument(0);
            saved.setId(3L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = userService.create(request);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUsernameExists() {
        final var request = new CreateUserRequest("jdoe", "Pass123!", "jdoe@erp.com",
                "John", "Doe", null, null);
        when(userRepository.existsByUsername("jdoe")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("jdoe");
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateEntityException_whenEmailExists() {
        final var request = new CreateUserRequest("jdoe2", "Pass123!", "jdoe@erp.com",
                "John", "Doe", null, null);
        when(userRepository.existsByUsername("jdoe2")).thenReturn(false);
        when(userRepository.existsByEmail("jdoe@erp.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("jdoe@erp.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturnUser_whenFoundById() {
        final var entity = createUser(1L, "jdoe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = userService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isEqualTo("jdoe");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFoundById() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(
                createUser(1L, "user1"), createUser(2L, "user2")));

        final var result = userService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateUser_whenIdExistsAndNameIsUnique() {
        final var entity = createUser(1L, "oldname");
        final var request = new CreateUserRequest("newname", null, "new@erp.com",
                "New", "Name", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.findByUsername("newname")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@erp.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = userService.update(1L, request);

        assertThat(result.username()).isEqualTo("newname");
    }

    @Test
    void shouldUpdateUser_whenUsernameUnchanged() {
        final var entity = createUser(1L, "samename");
        entity.setEmail("same@erp.com");
        final var request = new CreateUserRequest("samename", null, "same@erp.com",
                "Same", "Name", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.findByUsername("samename")).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail("same@erp.com")).thenReturn(Optional.of(entity));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = userService.update(1L, request);

        assertThat(result.username()).isEqualTo("samename");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentUser() {
        final var request = new CreateUserRequest("name", null, "e@e.com",
                "F", "L", null, null);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateUsernameConflicts() {
        final var entity = createUser(1L, "original");
        final var conflicting = createUser(2L, "taken");
        final var request = new CreateUserRequest("taken", null, "new@erp.com",
                "F", "L", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.findByUsername("taken")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> userService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("taken");
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateEmailConflicts() {
        final var entity = createUser(1L, "user1");
        entity.setEmail("user1@erp.com");
        final var conflicting = createUser(2L, "user2");
        conflicting.setEmail("taken@erp.com");
        final var request = new CreateUserRequest("user1", null, "taken@erp.com",
                "F", "L", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail("taken@erp.com")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> userService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("taken@erp.com");
    }

    @Test
    void shouldDeleteUser_whenIdExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRoleRepository).deleteByUserId(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentUser() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private User createUser(final Long id, final String username) {
        final var user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(username + "@erp.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setActive(true);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return user;
    }
}
