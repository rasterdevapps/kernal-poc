package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateUserPreferenceRequest;
import com.erp.kernel.navigation.entity.UserPreference;
import com.erp.kernel.navigation.repository.UserPreferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link UserPreferenceService}.
 */
@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceTest {

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @InjectMocks
    private UserPreferenceService userPreferenceService;

    @Test
    void shouldCreatePreference_whenNotExists() {
        final var request = new CreateUserPreferenceRequest(1L, 2L, "en",
                "yyyy-MM-dd", "HH:mm:ss", 20);
        when(userPreferenceRepository.existsByUserId(1L)).thenReturn(false);
        when(userPreferenceRepository.save(any(UserPreference.class))).thenAnswer(invocation -> {
            final var saved = invocation.<UserPreference>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = userPreferenceService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.locale()).isEqualTo("en");
        verify(userPreferenceRepository).save(any(UserPreference.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenPreferenceExistsForUser() {
        final var request = new CreateUserPreferenceRequest(1L, 2L, "en",
                "yyyy-MM-dd", "HH:mm:ss", 20);
        when(userPreferenceRepository.existsByUserId(1L)).thenReturn(true);

        assertThatThrownBy(() -> userPreferenceService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("1");
        verify(userPreferenceRepository, never()).save(any());
    }

    @Test
    void shouldReturnPreference_whenFoundById() {
        final var entity = createEntity(1L, 10L);
        when(userPreferenceRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = userPreferenceService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.userId()).isEqualTo(10L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenPreferenceNotFoundById() {
        when(userPreferenceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userPreferenceService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnPreference_whenFoundByUserId() {
        final var entity = createEntity(1L, 10L);
        when(userPreferenceRepository.findByUserId(10L)).thenReturn(Optional.of(entity));

        final var result = userPreferenceService.findByUserId(10L);

        assertThat(result.userId()).isEqualTo(10L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenPreferenceNotFoundByUserId() {
        when(userPreferenceRepository.findByUserId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userPreferenceService.findByUserId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldUpdatePreference_whenIdExists() {
        final var entity = createEntity(1L, 10L);
        final var request = new CreateUserPreferenceRequest(10L, 3L, "de",
                "dd.MM.yyyy", "HH:mm", 50);
        when(userPreferenceRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userPreferenceRepository.save(any(UserPreference.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = userPreferenceService.update(1L, request);

        assertThat(result).isNotNull();
        verify(userPreferenceRepository).save(any(UserPreference.class));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentPreference() {
        final var request = new CreateUserPreferenceRequest(1L, 2L, "en",
                "yyyy-MM-dd", "HH:mm:ss", 20);
        when(userPreferenceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userPreferenceService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeletePreference_whenIdExists() {
        when(userPreferenceRepository.existsById(1L)).thenReturn(true);

        userPreferenceService.delete(1L);

        verify(userPreferenceRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentPreference() {
        when(userPreferenceRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userPreferenceService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    private UserPreference createEntity(final Long id, final Long userId) {
        final var preference = new UserPreference();
        preference.setId(id);
        preference.setUserId(userId);
        preference.setThemeId(2L);
        preference.setLocale("en");
        preference.setDateFormat("yyyy-MM-dd");
        preference.setTimeFormat("HH:mm:ss");
        preference.setItemsPerPage(20);
        preference.setCreatedAt(Instant.now());
        preference.setUpdatedAt(Instant.now());
        return preference;
    }
}
