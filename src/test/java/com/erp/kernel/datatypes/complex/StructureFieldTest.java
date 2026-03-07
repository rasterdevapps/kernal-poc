package com.erp.kernel.datatypes.complex;

import com.erp.kernel.datatypes.elementary.ElementaryDataType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link StructureField}.
 */
class StructureFieldTest {

    @Test
    void shouldCreateStructureField_whenValidParameters() {
        final var field = new StructureField("BUKRS", ElementaryDataType.CHAR, 4, null, "Company code");

        assertThat(field.fieldName()).isEqualTo("BUKRS");
        assertThat(field.dataType()).isEqualTo(ElementaryDataType.CHAR);
        assertThat(field.maxLength()).isEqualTo(4);
        assertThat(field.decimalPlaces()).isNull();
        assertThat(field.description()).isEqualTo("Company code");
    }

    @Test
    void shouldThrowNullPointerException_whenFieldNameIsNull() {
        assertThatThrownBy(() -> new StructureField(null, ElementaryDataType.CHAR, 10, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("fieldName");
    }

    @Test
    void shouldThrowNullPointerException_whenDataTypeIsNull() {
        assertThatThrownBy(() -> new StructureField("FIELD", null, 10, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("dataType");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenFieldNameIsBlank() {
        assertThatThrownBy(() -> new StructureField("  ", ElementaryDataType.CHAR, 10, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fieldName");
    }
}
