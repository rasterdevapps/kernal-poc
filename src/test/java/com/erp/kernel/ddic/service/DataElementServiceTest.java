package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateDataElementRequest;
import com.erp.kernel.ddic.entity.DataElement;
import com.erp.kernel.ddic.entity.Domain;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.repository.DataElementRepository;
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
 * Tests for the {@link DataElementService}.
 */
@ExtendWith(MockitoExtension.class)
class DataElementServiceTest {

    @Mock
    private DataElementRepository dataElementRepository;

    @Mock
    private DomainRepository domainRepository;

    @InjectMocks
    private DataElementService dataElementService;

    @Test
    void shouldCreateDataElement_whenNameIsUnique() {
        final var request = new CreateDataElementRequest("CUST_NAME", 1L, "CN", "Cust Name", "Customer Name", "Desc");
        final var domain = createDomain(1L);
        when(dataElementRepository.existsByElementName("CUST_NAME")).thenReturn(false);
        when(domainRepository.findById(1L)).thenReturn(Optional.of(domain));
        when(dataElementRepository.save(any(DataElement.class))).thenAnswer(invocation -> {
            final var saved = invocation.<DataElement>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = dataElementService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.elementName()).isEqualTo("CUST_NAME");
        verify(dataElementRepository).save(any(DataElement.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenElementNameExists() {
        final var request = new CreateDataElementRequest("CUST_NAME", 1L, null, null, null, null);
        when(dataElementRepository.existsByElementName("CUST_NAME")).thenReturn(true);

        assertThatThrownBy(() -> dataElementService.create(request))
                .isInstanceOf(DuplicateEntityException.class);
        verify(dataElementRepository, never()).save(any());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDomainNotFoundOnCreate() {
        final var request = new CreateDataElementRequest("CUST_NAME", 99L, null, null, null, null);
        when(dataElementRepository.existsByElementName("CUST_NAME")).thenReturn(false);
        when(domainRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataElementService.create(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnDataElement_whenFoundById() {
        final var entity = createDataElement(1L, "ELEM_1");
        when(dataElementRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = dataElementService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.elementName()).isEqualTo("ELEM_1");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDataElementNotFoundById() {
        when(dataElementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataElementService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnAllDataElements() {
        when(dataElementRepository.findAll()).thenReturn(List.of(
                createDataElement(1L, "ELEM_1"),
                createDataElement(2L, "ELEM_2")));

        final var result = dataElementService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateDataElement_whenValid() {
        final var entity = createDataElement(1L, "OLD_NAME");
        final var newDomain = createDomain(2L);
        final var request = new CreateDataElementRequest("NEW_NAME", 2L, "N", "New", "New Name", "Desc");
        when(dataElementRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dataElementRepository.findByElementName("NEW_NAME")).thenReturn(Optional.empty());
        when(domainRepository.findById(2L)).thenReturn(Optional.of(newDomain));
        when(dataElementRepository.save(any(DataElement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = dataElementService.update(1L, request);

        assertThat(result.elementName()).isEqualTo("NEW_NAME");
    }

    @Test
    void shouldUpdateDataElement_whenNameUnchanged() {
        final var entity = createDataElement(1L, "SAME_NAME");
        final var domain = createDomain(1L);
        final var request = new CreateDataElementRequest("SAME_NAME", 1L, "S", "Same", "Same Name", "Desc");
        when(dataElementRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dataElementRepository.findByElementName("SAME_NAME")).thenReturn(Optional.of(entity));
        when(domainRepository.findById(1L)).thenReturn(Optional.of(domain));
        when(dataElementRepository.save(any(DataElement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = dataElementService.update(1L, request);

        assertThat(result.elementName()).isEqualTo("SAME_NAME");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentDataElement() {
        final var request = new CreateDataElementRequest("NAME", 1L, null, null, null, null);
        when(dataElementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataElementService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateElementNameConflicts() {
        final var entity = createDataElement(1L, "ORIGINAL");
        final var conflicting = createDataElement(2L, "TAKEN");
        final var request = new CreateDataElementRequest("TAKEN", 1L, null, null, null, null);
        when(dataElementRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dataElementRepository.findByElementName("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> dataElementService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdateDomainNotFound() {
        final var entity = createDataElement(1L, "ELEM");
        final var request = new CreateDataElementRequest("NEW_NAME", 99L, null, null, null, null);
        when(dataElementRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(dataElementRepository.findByElementName("NEW_NAME")).thenReturn(Optional.empty());
        when(domainRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataElementService.update(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeleteDataElement_whenIdExists() {
        when(dataElementRepository.existsById(1L)).thenReturn(true);

        dataElementService.delete(1L);

        verify(dataElementRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentDataElement() {
        when(dataElementRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> dataElementService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Domain createDomain(final Long id) {
        final var domain = new Domain();
        domain.setId(id);
        domain.setDomainName("DOMAIN_" + id);
        domain.setCreatedAt(Instant.now());
        domain.setUpdatedAt(Instant.now());
        return domain;
    }

    private DataElement createDataElement(final Long id, final String name) {
        final var domain = createDomain(1L);
        final var element = new DataElement();
        element.setId(id);
        element.setElementName(name);
        element.setDomain(domain);
        element.setShortLabel("Lbl");
        element.setMediumLabel("Label");
        element.setLongLabel("Long Label");
        element.setDescription("Description");
        element.setCreatedAt(Instant.now());
        element.setUpdatedAt(Instant.now());
        return element;
    }
}
