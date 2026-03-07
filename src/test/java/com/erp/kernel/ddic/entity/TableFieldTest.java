package com.erp.kernel.ddic.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link TableField} entity.
 */
class TableFieldTest {

    @Test
    void shouldSetAndGetTableDefinition() {
        final var table = new TableDefinition();
        table.setTableName("ORDERS");
        final var field = new TableField();
        field.setTableDefinition(table);
        assertThat(field.getTableDefinition()).isEqualTo(table);
    }

    @Test
    void shouldSetAndGetFieldName() {
        final var field = new TableField();
        field.setFieldName("ORDER_ID");
        assertThat(field.getFieldName()).isEqualTo("ORDER_ID");
    }

    @Test
    void shouldSetAndGetDataElement() {
        final var element = new DataElement();
        element.setElementName("ORDER_NUM");
        final var field = new TableField();
        field.setDataElement(element);
        assertThat(field.getDataElement()).isEqualTo(element);
    }

    @Test
    void shouldSetAndGetPosition() {
        final var field = new TableField();
        field.setPosition(3);
        assertThat(field.getPosition()).isEqualTo(3);
    }

    @Test
    void shouldSetAndGetKey() {
        final var field = new TableField();
        field.setKey(true);
        assertThat(field.isKey()).isTrue();
    }

    @Test
    void shouldSetAndGetNullable() {
        final var field = new TableField();
        field.setNullable(true);
        assertThat(field.isNullable()).isTrue();
    }

    @Test
    void shouldSetAndGetExtension() {
        final var field = new TableField();
        field.setExtension(true);
        assertThat(field.isExtension()).isTrue();
    }

    @Test
    void shouldDefaultBooleanFieldsToFalse() {
        final var field = new TableField();
        assertThat(field.isKey()).isFalse();
        assertThat(field.isNullable()).isFalse();
        assertThat(field.isExtension()).isFalse();
    }
}
