package com.erp.kernel.ddic.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link ExtensionFieldValue} entity.
 */
class ExtensionFieldValueTest {

    @Test
    void shouldSetAndGetTableName() {
        final var value = new ExtensionFieldValue();
        value.setTableName("CUSTOMERS");
        assertThat(value.getTableName()).isEqualTo("CUSTOMERS");
    }

    @Test
    void shouldSetAndGetRecordId() {
        final var value = new ExtensionFieldValue();
        value.setRecordId(42L);
        assertThat(value.getRecordId()).isEqualTo(42L);
    }

    @Test
    void shouldSetAndGetFieldName() {
        final var value = new ExtensionFieldValue();
        value.setFieldName("Z_CUSTOM_FIELD");
        assertThat(value.getFieldName()).isEqualTo("Z_CUSTOM_FIELD");
    }

    @Test
    void shouldSetAndGetFieldValue() {
        final var value = new ExtensionFieldValue();
        value.setFieldValue("custom value");
        assertThat(value.getFieldValue()).isEqualTo("custom value");
    }

    @Test
    void shouldAllowNullFieldValue() {
        final var value = new ExtensionFieldValue();
        value.setFieldValue(null);
        assertThat(value.getFieldValue()).isNull();
    }
}
