package com.erp.kernel.datatypes.reference;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link DataReference}.
 */
class DataReferenceTest {

    @Test
    void shouldCreateDataReference_whenValid() {
        final var ref = new DataReference("java.lang.String", "hello");

        assertThat(ref.targetType()).isEqualTo("java.lang.String");
        assertThat(ref.value()).isEqualTo("hello");
        assertThat(ref.isAssigned()).isTrue();
        assertThat(ref.getCategory()).isEqualTo(ReferenceCategory.DATA);
    }

    @Test
    void shouldCreateDataReference_whenValueIsNull() {
        final var ref = new DataReference("java.lang.Integer", null);

        assertThat(ref.isAssigned()).isFalse();
        assertThat(ref.value()).isNull();
    }

    @Test
    void shouldThrowNullPointerException_whenTargetTypeIsNull() {
        assertThatThrownBy(() -> new DataReference(null, "value"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("targetType");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenTargetTypeIsBlank() {
        assertThatThrownBy(() -> new DataReference("  ", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("targetType");
    }
}
