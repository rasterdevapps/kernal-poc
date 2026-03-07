package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateDomainRequest;
import com.erp.kernel.ddic.entity.Domain;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.repository.DomainRepository;
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
 * Tests for the {@link DomainService}.
 */
@ExtendWith(MockitoExtension.class)
class DomainServiceTest {

    @Mock
    private DomainRepository domainRepository;

    @InjectMocks
    private DomainService domainService;

    @Test
    void shouldCreateDomain_whenNameIsUnique() {
        final var request = new CreateDomainRequest("CHAR_30", "CHAR", 30, 0, "Character domain");
        when(domainRepository.existsByDomainName("CHAR_30")).thenReturn(false);
        when(domainRepository.save(any(Domain.class))).thenAnswer(invocation -> {
            final var saved = invocation.<Domain>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = domainService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.domainName()).isEqualTo("CHAR_30");
        assertThat(result.dataType()).isEqualTo("CHAR");
        verify(domainRepository).save(any(Domain.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenDomainNameExists() {
        final var request = new CreateDomainRequest("CHAR_30", "CHAR", 30, 0, "desc");
        when(domainRepository.existsByDomainName("CHAR_30")).thenReturn(true);

        assertThatThrownBy(() -> domainService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("CHAR_30");
        verify(domainRepository, never()).save(any());
    }

    @Test
    void shouldReturnDomain_whenFoundById() {
        final var entity = createDomain(1L, "INT_10");
        when(domainRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = domainService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.domainName()).isEqualTo("INT_10");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDomainNotFoundById() {
        when(domainRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domainService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllDomains() {
        when(domainRepository.findAll()).thenReturn(List.of(
                createDomain(1L, "CHAR_30"),
                createDomain(2L, "INT_10")));

        final var result = domainService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateDomain_whenIdExistsAndNameIsUnique() {
        final var entity = createDomain(1L, "OLD_NAME");
        final var request = new CreateDomainRequest("NEW_NAME", "INT", 10, 0, "Updated");
        when(domainRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(domainRepository.findByDomainName("NEW_NAME")).thenReturn(Optional.empty());
        when(domainRepository.save(any(Domain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = domainService.update(1L, request);

        assertThat(result.domainName()).isEqualTo("NEW_NAME");
    }

    @Test
    void shouldUpdateDomain_whenNameUnchanged() {
        final var entity = createDomain(1L, "SAME_NAME");
        final var request = new CreateDomainRequest("SAME_NAME", "INT", 10, 0, "Updated");
        when(domainRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(domainRepository.findByDomainName("SAME_NAME")).thenReturn(Optional.of(entity));
        when(domainRepository.save(any(Domain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = domainService.update(1L, request);

        assertThat(result.domainName()).isEqualTo("SAME_NAME");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentDomain() {
        final var request = new CreateDomainRequest("NAME", "CHAR", 10, 0, "desc");
        when(domainRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domainService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateNameConflicts() {
        final var entity = createDomain(1L, "ORIGINAL");
        final var conflicting = createDomain(2L, "TAKEN");
        final var request = new CreateDomainRequest("TAKEN", "CHAR", 10, 0, "desc");
        when(domainRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(domainRepository.findByDomainName("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> domainService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("TAKEN");
    }

    @Test
    void shouldDeleteDomain_whenIdExists() {
        when(domainRepository.existsById(1L)).thenReturn(true);

        domainService.delete(1L);

        verify(domainRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentDomain() {
        when(domainRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> domainService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Domain createDomain(final Long id, final String name) {
        final var domain = new Domain();
        domain.setId(id);
        domain.setDomainName(name);
        domain.setDataType("CHAR");
        domain.setMaxLength(30);
        domain.setDecimalPlaces(0);
        domain.setDescription("Test domain");
        domain.setCreatedAt(Instant.now());
        domain.setUpdatedAt(Instant.now());
        return domain;
    }
}
