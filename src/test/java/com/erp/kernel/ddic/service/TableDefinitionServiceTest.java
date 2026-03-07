package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateTableDefinitionRequest;
import com.erp.kernel.ddic.entity.TableDefinition;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.model.SchemaLevel;
import com.erp.kernel.ddic.repository.TableDefinitionRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link TableDefinitionService}.
 */
@ExtendWith(MockitoExtension.class)
class TableDefinitionServiceTest {

    @Mock
    private TableDefinitionRepository tableDefinitionRepository;

    @InjectMocks
    private TableDefinitionService tableDefinitionService;

    @Test
    void shouldCreateTableDefinition_whenNameIsUnique() {
        final var request = new CreateTableDefinitionRequest("CUSTOMERS", SchemaLevel.CONCEPTUAL, "Customer table", false);
        when(tableDefinitionRepository.existsByTableName("CUSTOMERS")).thenReturn(false);
        when(tableDefinitionRepository.save(any(TableDefinition.class))).thenAnswer(invocation -> {
            final var saved = invocation.<TableDefinition>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = tableDefinitionService.create(request);

        assertThat(result.tableName()).isEqualTo("CUSTOMERS");
        assertThat(result.schemaLevel()).isEqualTo(SchemaLevel.CONCEPTUAL);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenTableNameExists() {
        final var request = new CreateTableDefinitionRequest("CUSTOMERS", SchemaLevel.CONCEPTUAL, null, false);
        when(tableDefinitionRepository.existsByTableName("CUSTOMERS")).thenReturn(true);

        assertThatThrownBy(() -> tableDefinitionService.create(request))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    void shouldReturnTableDefinition_whenFoundById() {
        final var entity = createTableDefinition(1L, "ORDERS");
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = tableDefinitionService.findById(1L);

        assertThat(result.tableName()).isEqualTo("ORDERS");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenTableNotFoundById() {
        when(tableDefinitionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableDefinitionService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnAllTableDefinitions() {
        when(tableDefinitionRepository.findAll()).thenReturn(List.of(
                createTableDefinition(1L, "CUSTOMERS"),
                createTableDefinition(2L, "ORDERS")));

        final var result = tableDefinitionService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnTableDefinitionsBySchemaLevel() {
        when(tableDefinitionRepository.findBySchemaLevel(SchemaLevel.EXTERNAL))
                .thenReturn(List.of(createTableDefinition(1L, "V_CUSTOMERS")));

        final var result = tableDefinitionService.findBySchemaLevel(SchemaLevel.EXTERNAL);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().tableName()).isEqualTo("V_CUSTOMERS");
    }

    @Test
    void shouldUpdateTableDefinition_whenValid() {
        final var entity = createTableDefinition(1L, "OLD_TABLE");
        final var request = new CreateTableDefinitionRequest("NEW_TABLE", SchemaLevel.INTERNAL, "New desc", true);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tableDefinitionRepository.findByTableName("NEW_TABLE")).thenReturn(Optional.empty());
        when(tableDefinitionRepository.save(any(TableDefinition.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = tableDefinitionService.update(1L, request);

        assertThat(result.tableName()).isEqualTo("NEW_TABLE");
    }

    @Test
    void shouldUpdateTableDefinition_whenNameUnchanged() {
        final var entity = createTableDefinition(1L, "SAME_TABLE");
        final var request = new CreateTableDefinitionRequest("SAME_TABLE", SchemaLevel.INTERNAL, "Updated", true);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tableDefinitionRepository.findByTableName("SAME_TABLE")).thenReturn(Optional.of(entity));
        when(tableDefinitionRepository.save(any(TableDefinition.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = tableDefinitionService.update(1L, request);

        assertThat(result.tableName()).isEqualTo("SAME_TABLE");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentTable() {
        final var request = new CreateTableDefinitionRequest("T", SchemaLevel.EXTERNAL, null, false);
        when(tableDefinitionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableDefinitionService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateTableNameConflicts() {
        final var entity = createTableDefinition(1L, "ORIGINAL");
        final var conflicting = createTableDefinition(2L, "TAKEN");
        final var request = new CreateTableDefinitionRequest("TAKEN", SchemaLevel.EXTERNAL, null, false);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tableDefinitionRepository.findByTableName("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> tableDefinitionService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    void shouldDeleteTableDefinition_whenIdExists() {
        when(tableDefinitionRepository.existsById(1L)).thenReturn(true);

        tableDefinitionService.delete(1L);

        verify(tableDefinitionRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentTable() {
        when(tableDefinitionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> tableDefinitionService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private TableDefinition createTableDefinition(final Long id, final String name) {
        final var table = new TableDefinition();
        table.setId(id);
        table.setTableName(name);
        table.setSchemaLevel(SchemaLevel.CONCEPTUAL);
        table.setDescription("Test table");
        table.setClientSpecific(false);
        table.setCreatedAt(Instant.now());
        table.setUpdatedAt(Instant.now());
        return table;
    }
}
