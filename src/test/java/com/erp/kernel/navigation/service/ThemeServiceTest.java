package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateThemeRequest;
import com.erp.kernel.navigation.entity.Theme;
import com.erp.kernel.navigation.repository.ThemeRepository;
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
 * Tests for the {@link ThemeService}.
 */
@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    void shouldCreateTheme_whenNameIsUnique() {
        final var request = new CreateThemeRequest("Dark Mode", "A dark theme",
                "#000000", "#FFFFFF", false);
        when(themeRepository.existsByThemeName("Dark Mode")).thenReturn(false);
        when(themeRepository.save(any(Theme.class))).thenAnswer(invocation -> {
            final var saved = invocation.<Theme>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = themeService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.themeName()).isEqualTo("Dark Mode");
        assertThat(result.primaryColor()).isEqualTo("#000000");
        verify(themeRepository).save(any(Theme.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenThemeNameExists() {
        final var request = new CreateThemeRequest("Dark Mode", "A dark theme",
                "#000000", "#FFFFFF", false);
        when(themeRepository.existsByThemeName("Dark Mode")).thenReturn(true);

        assertThatThrownBy(() -> themeService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("Dark Mode");
        verify(themeRepository, never()).save(any());
    }

    @Test
    void shouldReturnTheme_whenFoundById() {
        final var entity = createTheme(1L, "Light Mode");
        when(themeRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = themeService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.themeName()).isEqualTo("Light Mode");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenThemeNotFoundById() {
        when(themeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllThemes() {
        when(themeRepository.findAll()).thenReturn(List.of(
                createTheme(1L, "Light"), createTheme(2L, "Dark")));

        final var result = themeService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateTheme_whenIdExistsAndNameIsUnique() {
        final var entity = createTheme(1L, "Old Name");
        final var request = new CreateThemeRequest("New Name", "Updated desc",
                "#111111", "#222222", true);
        when(themeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(themeRepository.findByThemeName("New Name")).thenReturn(Optional.empty());
        when(themeRepository.save(any(Theme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = themeService.update(1L, request);

        assertThat(result).isNotNull();
        verify(themeRepository).save(any(Theme.class));
    }

    @Test
    void shouldUpdateTheme_whenNameUnchanged() {
        final var entity = createTheme(1L, "Same Name");
        final var request = new CreateThemeRequest("Same Name", "Updated desc",
                "#111111", "#222222", false);
        when(themeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(themeRepository.findByThemeName("Same Name")).thenReturn(Optional.of(entity));
        when(themeRepository.save(any(Theme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = themeService.update(1L, request);

        assertThat(result).isNotNull();
        verify(themeRepository).save(any(Theme.class));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentTheme() {
        final var request = new CreateThemeRequest("Name", "Desc",
                "#000000", "#FFFFFF", false);
        when(themeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateNameConflicts() {
        final var entity = createTheme(1L, "Original");
        final var conflicting = createTheme(2L, "Taken");
        final var request = new CreateThemeRequest("Taken", "Desc",
                "#000000", "#FFFFFF", false);
        when(themeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(themeRepository.findByThemeName("Taken")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> themeService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("Taken");
    }

    @Test
    void shouldDeleteTheme_whenIdExists() {
        when(themeRepository.existsById(1L)).thenReturn(true);

        themeService.delete(1L);

        verify(themeRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentTheme() {
        when(themeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> themeService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    private Theme createTheme(final Long id, final String name) {
        final var theme = new Theme();
        theme.setId(id);
        theme.setThemeName(name);
        theme.setDescription("Test desc");
        theme.setPrimaryColor("#FF0000");
        theme.setSecondaryColor("#00FF00");
        theme.setActive(true);
        theme.setDefault(false);
        theme.setCreatedAt(Instant.now());
        theme.setUpdatedAt(Instant.now());
        return theme;
    }
}
