package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateExtensionFieldValueRequest;
import com.erp.kernel.ddic.entity.ExtensionFieldValue;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.ddic.repository.ExtensionFieldValueRepository;
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
 * Tests for the {@link ExtensionFieldService}.
 */
@ExtendWith(MockitoExtension.class)
class ExtensionFieldServiceTest {

    @Mock
    private ExtensionFieldValueRepository extensionFieldValueRepository;

    @InjectMocks
    private ExtensionFieldService extensionFieldService;

    @Test
    void shouldCreateNewExtensionFieldValue_whenNotExists() {
        final var request = new CreateExtensionFieldValueRequest("CUSTOMERS", 1L, "Z_CUSTOM", "value");
        when(extensionFieldValueRepository.findByTableNameAndRecordIdAndFieldName("CUSTOMERS", 1L, "Z_CUSTOM"))
                .thenReturn(Optional.empty());
        when(extensionFieldValueRepository.save(any(ExtensionFieldValue.class))).thenAnswer(invocation -> {
            final var saved = invocation.<ExtensionFieldValue>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = extensionFieldService.save(request);

        assertThat(result.fieldName()).isEqualTo("Z_CUSTOM");
        assertThat(result.fieldValue()).isEqualTo("value");
    }

    @Test
    void shouldUpdateExistingExtensionFieldValue_whenAlreadyExists() {
        final var request = new CreateExtensionFieldValueRequest("CUSTOMERS", 1L, "Z_CUSTOM", "new_value");
        final var existing = createExtensionFieldValue(1L);
        when(extensionFieldValueRepository.findByTableNameAndRecordIdAndFieldName("CUSTOMERS", 1L, "Z_CUSTOM"))
                .thenReturn(Optional.of(existing));
        when(extensionFieldValueRepository.save(any(ExtensionFieldValue.class))).thenAnswer(invocation -> {
            final var saved = invocation.<ExtensionFieldValue>getArgument(0);
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = extensionFieldService.save(request);

        assertThat(result).isNotNull();
        verify(extensionFieldValueRepository).save(existing);
    }

    @Test
    void shouldThrowValidationException_whenFieldNameDoesNotStartWithZ() {
        final var request = new CreateExtensionFieldValueRequest("CUSTOMERS", 1L, "INVALID_NAME", "value");

        assertThatThrownBy(() -> extensionFieldService.save(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Z_");
    }

    @Test
    void shouldReturnExtensionFieldValuesByRecord() {
        when(extensionFieldValueRepository.findByTableNameAndRecordId("CUSTOMERS", 1L))
                .thenReturn(List.of(createExtensionFieldValue(1L), createExtensionFieldValue(2L)));

        final var result = extensionFieldService.findByRecord("CUSTOMERS", 1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldDeleteExtensionFieldValue_whenIdExists() {
        when(extensionFieldValueRepository.existsById(1L)).thenReturn(true);

        extensionFieldService.delete(1L);

        verify(extensionFieldValueRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentValue() {
        when(extensionFieldValueRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> extensionFieldService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private ExtensionFieldValue createExtensionFieldValue(final Long id) {
        final var value = new ExtensionFieldValue();
        value.setId(id);
        value.setTableName("CUSTOMERS");
        value.setRecordId(1L);
        value.setFieldName("Z_CUSTOM");
        value.setFieldValue("test_value");
        value.setCreatedAt(Instant.now());
        value.setUpdatedAt(Instant.now());
        return value;
    }
}
