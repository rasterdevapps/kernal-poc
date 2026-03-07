package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateExtensionFieldValueRequest;
import com.erp.kernel.ddic.entity.ExtensionFieldValue;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link ExtensionFieldValueMapper}.
 */
class ExtensionFieldValueMapperTest {

    @Test
    void shouldConvertEntityToDto() {
        final var entity = createEntity(1L, "CUSTOMERS", 42L, "Z_CUSTOM", "value");
        final var dto = ExtensionFieldValueMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.tableName()).isEqualTo("CUSTOMERS");
        assertThat(dto.recordId()).isEqualTo(42L);
        assertThat(dto.fieldName()).isEqualTo("Z_CUSTOM");
        assertThat(dto.fieldValue()).isEqualTo("value");
        assertThat(dto.createdAt()).isNotNull();
        assertThat(dto.updatedAt()).isNotNull();
    }

    @Test
    void shouldConvertRequestToEntity() {
        final var request = new CreateExtensionFieldValueRequest("ORDERS", 10L, "Z_PRIORITY", "HIGH");
        final var entity = ExtensionFieldValueMapper.toEntity(request);

        assertThat(entity.getTableName()).isEqualTo("ORDERS");
        assertThat(entity.getRecordId()).isEqualTo(10L);
        assertThat(entity.getFieldName()).isEqualTo("Z_PRIORITY");
        assertThat(entity.getFieldValue()).isEqualTo("HIGH");
    }

    @Test
    void shouldUpdateExistingEntity() {
        final var entity = createEntity(1L, "OLD_TABLE", 1L, "Z_OLD", "old_value");
        final var request = new CreateExtensionFieldValueRequest("NEW_TABLE", 2L, "Z_NEW", "new_value");
        ExtensionFieldValueMapper.updateEntity(entity, request);

        assertThat(entity.getTableName()).isEqualTo("NEW_TABLE");
        assertThat(entity.getRecordId()).isEqualTo(2L);
        assertThat(entity.getFieldName()).isEqualTo("Z_NEW");
        assertThat(entity.getFieldValue()).isEqualTo("new_value");
    }

    @Test
    void shouldThrowNullPointerException_whenToDtoEntityIsNull() {
        assertThatThrownBy(() -> ExtensionFieldValueMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityRequestIsNull() {
        assertThatThrownBy(() -> ExtensionFieldValueMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateEntityIsNull() {
        final var request = new CreateExtensionFieldValueRequest("T", 1L, "Z_F", "v");
        assertThatThrownBy(() -> ExtensionFieldValueMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateRequestIsNull() {
        assertThatThrownBy(() -> ExtensionFieldValueMapper.updateEntity(new ExtensionFieldValue(), null))
                .isInstanceOf(NullPointerException.class);
    }

    private ExtensionFieldValue createEntity(final Long id, final String table, final Long recordId,
                                              final String fieldName, final String fieldValue) {
        final var entity = new ExtensionFieldValue();
        entity.setId(id);
        entity.setTableName(table);
        entity.setRecordId(recordId);
        entity.setFieldName(fieldName);
        entity.setFieldValue(fieldValue);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }
}
