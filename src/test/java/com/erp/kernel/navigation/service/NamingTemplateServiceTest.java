package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateNamingTemplateRequest;
import com.erp.kernel.navigation.entity.NamingTemplate;
import com.erp.kernel.navigation.repository.NamingTemplateRepository;
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
 * Tests for the {@link NamingTemplateService}.
 */
@ExtendWith(MockitoExtension.class)
class NamingTemplateServiceTest {

    @Mock
    private NamingTemplateRepository namingTemplateRepository;

    @InjectMocks
    private NamingTemplateService namingTemplateService;

    @Test
    void shouldCreateNamingTemplate_whenEntityTypeIsUnique() {
        final var request = new CreateNamingTemplateRequest("SCREEN",
                "{MODULE}_{SCREEN_TYPE}_{NAME}", "Screen naming convention", "ADMIN_LIST_USERS");
        when(namingTemplateRepository.existsByEntityType("SCREEN")).thenReturn(false);
        when(namingTemplateRepository.save(any(NamingTemplate.class))).thenAnswer(invocation -> {
            final var saved = invocation.<NamingTemplate>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = namingTemplateService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.entityType()).isEqualTo("SCREEN");
        assertThat(result.pattern()).isEqualTo("{MODULE}_{SCREEN_TYPE}_{NAME}");
        verify(namingTemplateRepository).save(any(NamingTemplate.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenEntityTypeExists() {
        final var request = new CreateNamingTemplateRequest("SCREEN",
                "{MODULE}_{SCREEN_TYPE}_{NAME}", "Screen naming convention", "ADMIN_LIST_USERS");
        when(namingTemplateRepository.existsByEntityType("SCREEN")).thenReturn(true);

        assertThatThrownBy(() -> namingTemplateService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("SCREEN");
        verify(namingTemplateRepository, never()).save(any());
    }

    @Test
    void shouldReturnNamingTemplate_whenFoundById() {
        final var entity = createEntity(1L, "SCREEN");
        when(namingTemplateRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = namingTemplateService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.entityType()).isEqualTo("SCREEN");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenNamingTemplateNotFoundById() {
        when(namingTemplateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> namingTemplateService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllNamingTemplates() {
        when(namingTemplateRepository.findAll()).thenReturn(List.of(
                createEntity(1L, "SCREEN"), createEntity(2L, "COMPONENT")));

        final var result = namingTemplateService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateNamingTemplate_whenIdExistsAndEntityTypeIsUnique() {
        final var entity = createEntity(1L, "OLD_TYPE");
        final var request = new CreateNamingTemplateRequest("NEW_TYPE",
                "{NEW_PATTERN}", "Updated description", "NEW_EXAMPLE");
        when(namingTemplateRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(namingTemplateRepository.findByEntityType("NEW_TYPE")).thenReturn(Optional.empty());
        when(namingTemplateRepository.save(any(NamingTemplate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = namingTemplateService.update(1L, request);

        assertThat(result).isNotNull();
        verify(namingTemplateRepository).save(any(NamingTemplate.class));
    }

    @Test
    void shouldUpdateNamingTemplate_whenEntityTypeUnchanged() {
        final var entity = createEntity(1L, "SCREEN");
        final var request = new CreateNamingTemplateRequest("SCREEN",
                "{UPDATED_PATTERN}", "Updated description", "UPDATED_EXAMPLE");
        when(namingTemplateRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(namingTemplateRepository.findByEntityType("SCREEN")).thenReturn(Optional.of(entity));
        when(namingTemplateRepository.save(any(NamingTemplate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = namingTemplateService.update(1L, request);

        assertThat(result).isNotNull();
        verify(namingTemplateRepository).save(any(NamingTemplate.class));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentNamingTemplate() {
        final var request = new CreateNamingTemplateRequest("SCREEN",
                "{MODULE}_{SCREEN_TYPE}_{NAME}", "Description", "ADMIN_LIST_USERS");
        when(namingTemplateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> namingTemplateService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateEntityTypeConflicts() {
        final var entity = createEntity(1L, "SCREEN");
        final var conflicting = createEntity(2L, "TAKEN_TYPE");
        final var request = new CreateNamingTemplateRequest("TAKEN_TYPE",
                "{PATTERN}", "Description", "EXAMPLE");
        when(namingTemplateRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(namingTemplateRepository.findByEntityType("TAKEN_TYPE"))
                .thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> namingTemplateService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("TAKEN_TYPE");
    }

    @Test
    void shouldDeleteNamingTemplate_whenIdExists() {
        when(namingTemplateRepository.existsById(1L)).thenReturn(true);

        namingTemplateService.delete(1L);

        verify(namingTemplateRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentNamingTemplate() {
        when(namingTemplateRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> namingTemplateService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    private NamingTemplate createEntity(final Long id, final String entityType) {
        final var template = new NamingTemplate();
        template.setId(id);
        template.setEntityType(entityType);
        template.setPattern("{MODULE}_{NAME}");
        template.setDescription("Test description");
        template.setExample("ADMIN_USERS");
        template.setCreatedAt(Instant.now());
        template.setUpdatedAt(Instant.now());
        return template;
    }
}
