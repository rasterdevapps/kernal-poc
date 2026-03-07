package com.erp.kernel.ddic.entity;

import com.erp.kernel.ddic.model.SchemaLevel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link TableDefinition} entity.
 */
class TableDefinitionTest {

    @Test
    void shouldSetAndGetTableName() {
        final var table = new TableDefinition();
        table.setTableName("CUSTOMERS");
        assertThat(table.getTableName()).isEqualTo("CUSTOMERS");
    }

    @Test
    void shouldSetAndGetSchemaLevel() {
        final var table = new TableDefinition();
        table.setSchemaLevel(SchemaLevel.CONCEPTUAL);
        assertThat(table.getSchemaLevel()).isEqualTo(SchemaLevel.CONCEPTUAL);
    }

    @Test
    void shouldSetAndGetDescription() {
        final var table = new TableDefinition();
        table.setDescription("Customer master data");
        assertThat(table.getDescription()).isEqualTo("Customer master data");
    }

    @Test
    void shouldSetAndGetClientSpecific() {
        final var table = new TableDefinition();
        table.setClientSpecific(true);
        assertThat(table.isClientSpecific()).isTrue();
    }

    @Test
    void shouldDefaultClientSpecificToFalse() {
        final var table = new TableDefinition();
        assertThat(table.isClientSpecific()).isFalse();
    }
}
