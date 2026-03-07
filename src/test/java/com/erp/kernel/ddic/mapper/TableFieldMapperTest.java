package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateTableFieldRequest;
import com.erp.kernel.ddic.entity.DataElement;
import com.erp.kernel.ddic.entity.TableDefinition;
import com.erp.kernel.ddic.entity.TableField;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link TableFieldMapper}.
 */
class TableFieldMapperTest {

    @Test
    void shouldConvertEntityToDto() {
        final var table = createTableDefinition(1L);
        final var element = createDataElement(2L, "CUST_NAME");
        final var entity = createTableFieldEntity(3L, table, "CUSTOMER_NAME", element, 1, true, false, false);
        final var dto = TableFieldMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(3L);
        assertThat(dto.tableDefinitionId()).isEqualTo(1L);
        assertThat(dto.fieldName()).isEqualTo("CUSTOMER_NAME");
        assertThat(dto.dataElementId()).isEqualTo(2L);
        assertThat(dto.dataElementName()).isEqualTo("CUST_NAME");
        assertThat(dto.position()).isEqualTo(1);
        assertThat(dto.key()).isTrue();
        assertThat(dto.nullable()).isFalse();
        assertThat(dto.extension()).isFalse();
    }

    @Test
    void shouldConvertRequestToEntity() {
        final var table = createTableDefinition(1L);
        final var element = createDataElement(2L, "ORDER_NUM");
        final var request = new CreateTableFieldRequest(1L, "ORDER_ID", 2L, 1, true, false, false);
        final var entity = TableFieldMapper.toEntity(request, table, element);

        assertThat(entity.getTableDefinition()).isEqualTo(table);
        assertThat(entity.getFieldName()).isEqualTo("ORDER_ID");
        assertThat(entity.getDataElement()).isEqualTo(element);
        assertThat(entity.getPosition()).isEqualTo(1);
        assertThat(entity.isKey()).isTrue();
        assertThat(entity.isNullable()).isFalse();
        assertThat(entity.isExtension()).isFalse();
    }

    @Test
    void shouldUpdateExistingEntity() {
        final var oldTable = createTableDefinition(1L);
        final var newTable = createTableDefinition(2L);
        final var oldElement = createDataElement(3L, "OLD_ELEM");
        final var newElement = createDataElement(4L, "NEW_ELEM");
        final var entity = createTableFieldEntity(5L, oldTable, "OLD_FIELD", oldElement, 1, true, false, false);
        final var request = new CreateTableFieldRequest(2L, "NEW_FIELD", 4L, 2, false, true, true);
        TableFieldMapper.updateEntity(entity, request, newTable, newElement);

        assertThat(entity.getTableDefinition()).isEqualTo(newTable);
        assertThat(entity.getFieldName()).isEqualTo("NEW_FIELD");
        assertThat(entity.getDataElement()).isEqualTo(newElement);
        assertThat(entity.getPosition()).isEqualTo(2);
        assertThat(entity.isKey()).isFalse();
        assertThat(entity.isNullable()).isTrue();
        assertThat(entity.isExtension()).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenToDtoEntityIsNull() {
        assertThatThrownBy(() -> TableFieldMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityRequestIsNull() {
        assertThatThrownBy(() -> TableFieldMapper.toEntity(null, new TableDefinition(), new DataElement()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityTableIsNull() {
        final var request = new CreateTableFieldRequest(1L, "F", 1L, 1, false, true, false);
        assertThatThrownBy(() -> TableFieldMapper.toEntity(request, null, new DataElement()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityDataElementIsNull() {
        final var request = new CreateTableFieldRequest(1L, "F", 1L, 1, false, true, false);
        assertThatThrownBy(() -> TableFieldMapper.toEntity(request, new TableDefinition(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateEntityIsNull() {
        final var request = new CreateTableFieldRequest(1L, "F", 1L, 1, false, true, false);
        assertThatThrownBy(() -> TableFieldMapper.updateEntity(null, request, new TableDefinition(), new DataElement()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateRequestIsNull() {
        assertThatThrownBy(() -> TableFieldMapper.updateEntity(new TableField(), null, new TableDefinition(), new DataElement()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateTableIsNull() {
        final var request = new CreateTableFieldRequest(1L, "F", 1L, 1, false, true, false);
        assertThatThrownBy(() -> TableFieldMapper.updateEntity(new TableField(), request, null, new DataElement()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateDataElementIsNull() {
        final var request = new CreateTableFieldRequest(1L, "F", 1L, 1, false, true, false);
        assertThatThrownBy(() -> TableFieldMapper.updateEntity(new TableField(), request, new TableDefinition(), null))
                .isInstanceOf(NullPointerException.class);
    }

    private TableDefinition createTableDefinition(final Long id) {
        final var table = new TableDefinition();
        table.setId(id);
        table.setCreatedAt(Instant.now());
        table.setUpdatedAt(Instant.now());
        return table;
    }

    private DataElement createDataElement(final Long id, final String name) {
        final var element = new DataElement();
        element.setId(id);
        element.setElementName(name);
        element.setCreatedAt(Instant.now());
        element.setUpdatedAt(Instant.now());
        return element;
    }

    private TableField createTableFieldEntity(final Long id, final TableDefinition table,
                                               final String fieldName, final DataElement element,
                                               final int position, final boolean key,
                                               final boolean nullable, final boolean extension) {
        final var field = new TableField();
        field.setId(id);
        field.setTableDefinition(table);
        field.setFieldName(fieldName);
        field.setDataElement(element);
        field.setPosition(position);
        field.setKey(key);
        field.setNullable(nullable);
        field.setExtension(extension);
        field.setCreatedAt(Instant.now());
        field.setUpdatedAt(Instant.now());
        return field;
    }
}
