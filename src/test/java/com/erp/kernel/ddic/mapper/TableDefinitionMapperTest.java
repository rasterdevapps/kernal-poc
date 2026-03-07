package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateTableDefinitionRequest;
import com.erp.kernel.ddic.entity.TableDefinition;
import com.erp.kernel.ddic.model.SchemaLevel;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link TableDefinitionMapper}.
 */
class TableDefinitionMapperTest {

    @Test
    void shouldConvertEntityToDto() {
        final var entity = createTableDefinitionEntity(1L, "CUSTOMERS", SchemaLevel.CONCEPTUAL, "Customer table", true);
        final var dto = TableDefinitionMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.tableName()).isEqualTo("CUSTOMERS");
        assertThat(dto.schemaLevel()).isEqualTo(SchemaLevel.CONCEPTUAL);
        assertThat(dto.description()).isEqualTo("Customer table");
        assertThat(dto.clientSpecific()).isTrue();
        assertThat(dto.createdAt()).isNotNull();
        assertThat(dto.updatedAt()).isNotNull();
    }

    @Test
    void shouldConvertRequestToEntity() {
        final var request = new CreateTableDefinitionRequest("ORDERS", SchemaLevel.INTERNAL, "Order table", false);
        final var entity = TableDefinitionMapper.toEntity(request);

        assertThat(entity.getTableName()).isEqualTo("ORDERS");
        assertThat(entity.getSchemaLevel()).isEqualTo(SchemaLevel.INTERNAL);
        assertThat(entity.getDescription()).isEqualTo("Order table");
        assertThat(entity.isClientSpecific()).isFalse();
    }

    @Test
    void shouldUpdateExistingEntity() {
        final var entity = createTableDefinitionEntity(1L, "OLD_TABLE", SchemaLevel.EXTERNAL, "Old desc", false);
        final var request = new CreateTableDefinitionRequest("NEW_TABLE", SchemaLevel.CONCEPTUAL, "New desc", true);
        TableDefinitionMapper.updateEntity(entity, request);

        assertThat(entity.getTableName()).isEqualTo("NEW_TABLE");
        assertThat(entity.getSchemaLevel()).isEqualTo(SchemaLevel.CONCEPTUAL);
        assertThat(entity.getDescription()).isEqualTo("New desc");
        assertThat(entity.isClientSpecific()).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenToDtoEntityIsNull() {
        assertThatThrownBy(() -> TableDefinitionMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityRequestIsNull() {
        assertThatThrownBy(() -> TableDefinitionMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateEntityIsNull() {
        final var request = new CreateTableDefinitionRequest("T", SchemaLevel.EXTERNAL, null, false);
        assertThatThrownBy(() -> TableDefinitionMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateRequestIsNull() {
        assertThatThrownBy(() -> TableDefinitionMapper.updateEntity(new TableDefinition(), null))
                .isInstanceOf(NullPointerException.class);
    }

    private TableDefinition createTableDefinitionEntity(final Long id, final String name,
                                                         final SchemaLevel level, final String desc,
                                                         final boolean clientSpecific) {
        final var table = new TableDefinition();
        table.setId(id);
        table.setTableName(name);
        table.setSchemaLevel(level);
        table.setDescription(desc);
        table.setClientSpecific(clientSpecific);
        table.setCreatedAt(Instant.now());
        table.setUpdatedAt(Instant.now());
        return table;
    }
}
