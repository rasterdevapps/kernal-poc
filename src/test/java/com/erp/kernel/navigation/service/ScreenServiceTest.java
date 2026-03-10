package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateScreenRequest;
import com.erp.kernel.navigation.entity.Screen;
import com.erp.kernel.navigation.repository.ScreenRepository;
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
 * Tests for the {@link ScreenService}.
 */
@ExtendWith(MockitoExtension.class)
class ScreenServiceTest {

    @Mock
    private ScreenRepository screenRepository;

    @InjectMocks
    private ScreenService screenService;

    @Test
    void shouldCreateScreen_whenScreenIdIsUnique() {
        final var request = new CreateScreenRequest("SCR_ADMIN_USERS", "User Management",
                "Admin user list", "ADMIN", 1L, "LIST");
        when(screenRepository.existsByScreenId("SCR_ADMIN_USERS")).thenReturn(false);
        when(screenRepository.save(any(Screen.class))).thenAnswer(invocation -> {
            final var saved = invocation.<Screen>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = screenService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.screenId()).isEqualTo("SCR_ADMIN_USERS");
        assertThat(result.module()).isEqualTo("ADMIN");
        verify(screenRepository).save(any(Screen.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenScreenIdExists() {
        final var request = new CreateScreenRequest("SCR_ADMIN_USERS", "User Management",
                "Admin user list", "ADMIN", 1L, "LIST");
        when(screenRepository.existsByScreenId("SCR_ADMIN_USERS")).thenReturn(true);

        assertThatThrownBy(() -> screenService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("SCR_ADMIN_USERS");
        verify(screenRepository, never()).save(any());
    }

    @Test
    void shouldReturnScreen_whenFoundById() {
        final var entity = createEntity(1L, "SCR_ADMIN_USERS");
        when(screenRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = screenService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.screenId()).isEqualTo("SCR_ADMIN_USERS");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenScreenNotFoundById() {
        when(screenRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> screenService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllScreens() {
        when(screenRepository.findAll()).thenReturn(List.of(
                createEntity(1L, "SCR_ADMIN_USERS"), createEntity(2L, "SCR_ADMIN_ROLES")));

        final var result = screenService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnScreensByModule() {
        when(screenRepository.findByModule("ADMIN")).thenReturn(List.of(
                createEntity(1L, "SCR_ADMIN_USERS"), createEntity(2L, "SCR_ADMIN_ROLES")));

        final var result = screenService.findByModule("ADMIN");

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateScreen_whenIdExistsAndScreenIdIsUnique() {
        final var entity = createEntity(1L, "OLD_SCREEN");
        final var request = new CreateScreenRequest("NEW_SCREEN", "New Title",
                "New description", "FINANCE", 2L, "DETAIL");
        when(screenRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(screenRepository.findByScreenId("NEW_SCREEN")).thenReturn(Optional.empty());
        when(screenRepository.save(any(Screen.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = screenService.update(1L, request);

        assertThat(result).isNotNull();
        verify(screenRepository).save(any(Screen.class));
    }

    @Test
    void shouldUpdateScreen_whenScreenIdUnchanged() {
        final var entity = createEntity(1L, "SCR_ADMIN_USERS");
        final var request = new CreateScreenRequest("SCR_ADMIN_USERS", "Updated Title",
                "Updated description", "ADMIN", 1L, "LIST");
        when(screenRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(screenRepository.findByScreenId("SCR_ADMIN_USERS")).thenReturn(Optional.of(entity));
        when(screenRepository.save(any(Screen.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = screenService.update(1L, request);

        assertThat(result).isNotNull();
        verify(screenRepository).save(any(Screen.class));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentScreen() {
        final var request = new CreateScreenRequest("SCR_ADMIN_USERS", "Title",
                "Description", "ADMIN", 1L, "LIST");
        when(screenRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> screenService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateScreenIdConflicts() {
        final var entity = createEntity(1L, "SCR_ADMIN_USERS");
        final var conflicting = createEntity(2L, "TAKEN_SCREEN");
        final var request = new CreateScreenRequest("TAKEN_SCREEN", "Title",
                "Description", "ADMIN", 1L, "LIST");
        when(screenRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(screenRepository.findByScreenId("TAKEN_SCREEN"))
                .thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> screenService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("TAKEN_SCREEN");
    }

    @Test
    void shouldDeleteScreen_whenIdExists() {
        when(screenRepository.existsById(1L)).thenReturn(true);

        screenService.delete(1L);

        verify(screenRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentScreen() {
        when(screenRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> screenService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    private Screen createEntity(final Long id, final String screenId) {
        final var screen = new Screen();
        screen.setId(id);
        screen.setScreenId(screenId);
        screen.setTitle("Test Screen");
        screen.setDescription("Test description");
        screen.setModule("ADMIN");
        screen.setTcodeId(1L);
        screen.setScreenType("LIST");
        screen.setCreatedAt(Instant.now());
        screen.setUpdatedAt(Instant.now());
        return screen;
    }
}
