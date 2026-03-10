package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateTCodeRequest;
import com.erp.kernel.navigation.entity.TCode;
import com.erp.kernel.navigation.repository.TCodeRepository;
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
 * Tests for the {@link TCodeService}.
 */
@ExtendWith(MockitoExtension.class)
class TCodeServiceTest {

    @Mock
    private TCodeRepository tCodeRepository;

    @InjectMocks
    private TCodeService tCodeService;

    @Test
    void shouldCreateTCode_whenCodeIsUnique() {
        final var request = new CreateTCodeRequest("SU01", "User Maintenance",
                "SECURITY", "/security/users", "person");
        when(tCodeRepository.existsByCode("SU01")).thenReturn(false);
        when(tCodeRepository.save(any(TCode.class))).thenAnswer(invocation -> {
            final var saved = invocation.<TCode>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = tCodeService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.code()).isEqualTo("SU01");
        assertThat(result.module()).isEqualTo("SECURITY");
        verify(tCodeRepository).save(any(TCode.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenCodeExists() {
        final var request = new CreateTCodeRequest("SU01", "User Maintenance",
                "SECURITY", "/security/users", "person");
        when(tCodeRepository.existsByCode("SU01")).thenReturn(true);

        assertThatThrownBy(() -> tCodeService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("SU01");
        verify(tCodeRepository, never()).save(any());
    }

    @Test
    void shouldReturnTCode_whenFoundById() {
        final var entity = createTCode(1L, "SU01");
        when(tCodeRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = tCodeService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.code()).isEqualTo("SU01");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenTCodeNotFoundById() {
        when(tCodeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tCodeService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllTCodes() {
        when(tCodeRepository.findAll()).thenReturn(List.of(
                createTCode(1L, "SU01"), createTCode(2L, "SE11")));

        final var result = tCodeService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnTCode_whenFoundByCode() {
        final var entity = createTCode(1L, "SU01");
        when(tCodeRepository.findByCode("SU01")).thenReturn(Optional.of(entity));

        final var result = tCodeService.findByCode("SU01");

        assertThat(result.code()).isEqualTo("SU01");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenTCodeNotFoundByCode() {
        when(tCodeRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tCodeService.findByCode("INVALID"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("INVALID");
    }

    @Test
    void shouldReturnTCodesByModule() {
        when(tCodeRepository.findByModule("ADMIN")).thenReturn(List.of(
                createTCode(1L, "SU01"), createTCode(2L, "SU02")));

        final var result = tCodeService.findByModule("ADMIN");

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnActiveTCodes() {
        when(tCodeRepository.findByActiveTrue()).thenReturn(List.of(
                createTCode(1L, "SU01")));

        final var result = tCodeService.findAllActive();

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldUpdateTCode_whenIdExistsAndCodeIsUnique() {
        final var entity = createTCode(1L, "OLD01");
        final var request = new CreateTCodeRequest("NEW01", "New Description",
                "ADMIN", "/admin/new", "settings");
        when(tCodeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tCodeRepository.findByCode("NEW01")).thenReturn(Optional.empty());
        when(tCodeRepository.save(any(TCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = tCodeService.update(1L, request);

        assertThat(result).isNotNull();
        verify(tCodeRepository).save(any(TCode.class));
    }

    @Test
    void shouldUpdateTCode_whenCodeUnchanged() {
        final var entity = createTCode(1L, "SU01");
        final var request = new CreateTCodeRequest("SU01", "Updated Description",
                "SECURITY", "/security/users", "person");
        when(tCodeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tCodeRepository.findByCode("SU01")).thenReturn(Optional.of(entity));
        when(tCodeRepository.save(any(TCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = tCodeService.update(1L, request);

        assertThat(result).isNotNull();
        verify(tCodeRepository).save(any(TCode.class));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentTCode() {
        final var request = new CreateTCodeRequest("SU01", "Desc",
                "ADMIN", "/admin/test", "settings");
        when(tCodeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tCodeService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateCodeConflicts() {
        final var entity = createTCode(1L, "SU01");
        final var conflicting = createTCode(2L, "TAKEN");
        final var request = new CreateTCodeRequest("TAKEN", "Desc",
                "ADMIN", "/admin/test", "settings");
        when(tCodeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tCodeRepository.findByCode("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> tCodeService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("TAKEN");
    }

    @Test
    void shouldDeleteTCode_whenIdExists() {
        when(tCodeRepository.existsById(1L)).thenReturn(true);

        tCodeService.delete(1L);

        verify(tCodeRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentTCode() {
        when(tCodeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> tCodeService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    private TCode createTCode(final Long id, final String code) {
        final var tcode = new TCode();
        tcode.setId(id);
        tcode.setCode(code);
        tcode.setDescription("Test T-Code");
        tcode.setModule("ADMIN");
        tcode.setRoute("/admin/test");
        tcode.setIcon("settings");
        tcode.setActive(true);
        tcode.setCreatedAt(Instant.now());
        tcode.setUpdatedAt(Instant.now());
        return tcode;
    }
}
