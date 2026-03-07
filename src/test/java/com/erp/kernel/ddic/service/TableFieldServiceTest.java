package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateTableFieldRequest;
import com.erp.kernel.ddic.entity.DataElement;
import com.erp.kernel.ddic.entity.Domain;
import com.erp.kernel.ddic.entity.TableDefinition;
import com.erp.kernel.ddic.entity.TableField;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.ddic.repository.DataElementRepository;
import com.erp.kernel.ddic.repository.TableDefinitionRepository;
import com.erp.kernel.ddic.repository.TableFieldRepository;
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
 * Tests for the {@link TableFieldService}.
 */
@ExtendWith(MockitoExtension.class)
class TableFieldServiceTest {

    @Mock
    private TableFieldRepository tableFieldRepository;

    @Mock
    private TableDefinitionRepository tableDefinitionRepository;

    @Mock
    private DataElementRepository dataElementRepository;

    @InjectMocks
    private TableFieldService tableFieldService;

    @Test
    void shouldCreateTableField_whenValid() {
        final var request = new CreateTableFieldRequest(1L, "CUSTOMER_ID", 2L, 1, true, false, false);
        final var table = createTableDefinition(1L);
        final var element = createDataElement(2L);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(table));
        when(dataElementRepository.findById(2L)).thenReturn(Optional.of(element));
        when(tableFieldRepository.existsByTableDefinitionIdAndFieldName(1L, "CUSTOMER_ID")).thenReturn(false);
        when(tableFieldRepository.save(any(TableField.class))).thenAnswer(invocation -> {
            final var saved = invocation.<TableField>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = tableFieldService.create(request);

        assertThat(result.fieldName()).isEqualTo("CUSTOMER_ID");
        assertThat(result.key()).isTrue();
    }

    @Test
    void shouldCreateExtensionField_whenNameStartsWithZ() {
        final var request = new CreateTableFieldRequest(1L, "Z_CUSTOM", 2L, 10, false, true, true);
        final var table = createTableDefinition(1L);
        final var element = createDataElement(2L);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(table));
        when(dataElementRepository.findById(2L)).thenReturn(Optional.of(element));
        when(tableFieldRepository.existsByTableDefinitionIdAndFieldName(1L, "Z_CUSTOM")).thenReturn(false);
        when(tableFieldRepository.save(any(TableField.class))).thenAnswer(invocation -> {
            final var saved = invocation.<TableField>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = tableFieldService.create(request);

        assertThat(result.extension()).isTrue();
    }

    @Test
    void shouldThrowValidationException_whenExtensionFieldNameInvalid() {
        final var request = new CreateTableFieldRequest(1L, "INVALID_NAME", 2L, 1, false, true, true);

        assertThatThrownBy(() -> tableFieldService.create(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Z_");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenTableDefinitionNotFound() {
        final var request = new CreateTableFieldRequest(99L, "FIELD", 1L, 1, false, true, false);
        when(tableDefinitionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableFieldService.create(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDataElementNotFound() {
        final var request = new CreateTableFieldRequest(1L, "FIELD", 99L, 1, false, true, false);
        final var table = createTableDefinition(1L);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(table));
        when(dataElementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableFieldService.create(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenFieldNameExists() {
        final var request = new CreateTableFieldRequest(1L, "EXISTING", 2L, 1, false, true, false);
        final var table = createTableDefinition(1L);
        final var element = createDataElement(2L);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(table));
        when(dataElementRepository.findById(2L)).thenReturn(Optional.of(element));
        when(tableFieldRepository.existsByTableDefinitionIdAndFieldName(1L, "EXISTING")).thenReturn(true);

        assertThatThrownBy(() -> tableFieldService.create(request))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    void shouldReturnTableField_whenFoundById() {
        final var entity = createTableField(1L);
        when(tableFieldRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = tableFieldService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenFieldNotFoundById() {
        when(tableFieldRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableFieldService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnFieldsByTableDefinitionId() {
        when(tableFieldRepository.findByTableDefinitionIdOrderByPositionAsc(1L))
                .thenReturn(List.of(createTableField(1L), createTableField(2L)));

        final var result = tableFieldService.findByTableDefinitionId(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnExtensionFieldsByTableDefinitionId() {
        when(tableFieldRepository.findByTableDefinitionIdAndExtensionTrue(1L))
                .thenReturn(List.of(createTableField(1L)));

        final var result = tableFieldService.findExtensionFields(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldDeleteTableField_whenIdExists() {
        when(tableFieldRepository.existsById(1L)).thenReturn(true);

        tableFieldService.delete(1L);

        verify(tableFieldRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentField() {
        when(tableFieldRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> tableFieldService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private TableDefinition createTableDefinition(final Long id) {
        final var table = new TableDefinition();
        table.setId(id);
        table.setTableName("TABLE_" + id);
        table.setCreatedAt(Instant.now());
        table.setUpdatedAt(Instant.now());
        return table;
    }

    private DataElement createDataElement(final Long id) {
        final var domain = new Domain();
        domain.setId(1L);
        domain.setDomainName("DOMAIN");
        domain.setCreatedAt(Instant.now());
        domain.setUpdatedAt(Instant.now());
        final var element = new DataElement();
        element.setId(id);
        element.setElementName("ELEMENT_" + id);
        element.setDomain(domain);
        element.setCreatedAt(Instant.now());
        element.setUpdatedAt(Instant.now());
        return element;
    }

    private TableField createTableField(final Long id) {
        final var field = new TableField();
        field.setId(id);
        field.setTableDefinition(createTableDefinition(1L));
        field.setFieldName("FIELD_" + id);
        field.setDataElement(createDataElement(1L));
        field.setPosition(1);
        field.setKey(false);
        field.setNullable(true);
        field.setExtension(false);
        field.setCreatedAt(Instant.now());
        field.setUpdatedAt(Instant.now());
        return field;
    }
}
