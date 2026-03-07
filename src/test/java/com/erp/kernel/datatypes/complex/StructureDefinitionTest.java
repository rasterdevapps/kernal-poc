package com.erp.kernel.datatypes.complex;

import com.erp.kernel.datatypes.elementary.ElementaryDataType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link StructureDefinition}.
 */
class StructureDefinitionTest {

    @Test
    void shouldCreateStructure_whenValidParameters() {
        final var fields = List.of(
                new StructureField("BUKRS", ElementaryDataType.CHAR, 4, null, "Company code"),
                new StructureField("GJAHR", ElementaryDataType.NUMC, 4, null, "Fiscal year")
        );
        final var structure = new StructureDefinition("S_COMPANY", "Company structure", fields);

        assertThat(structure.getStructureName()).isEqualTo("S_COMPANY");
        assertThat(structure.getDescription()).isEqualTo("Company structure");
        assertThat(structure.getFieldCount()).isEqualTo(2);
        assertThat(structure.getFields()).hasSize(2);
    }

    @Test
    void shouldReturnField_whenFieldExists() {
        final var field = new StructureField("BUKRS", ElementaryDataType.CHAR, 4, null, null);
        final var structure = new StructureDefinition("TEST", null, List.of(field));

        assertThat(structure.getField("BUKRS")).isPresent();
        assertThat(structure.getField("BUKRS").get().dataType()).isEqualTo(ElementaryDataType.CHAR);
    }

    @Test
    void shouldReturnEmpty_whenFieldDoesNotExist() {
        final var field = new StructureField("BUKRS", ElementaryDataType.CHAR, 4, null, null);
        final var structure = new StructureDefinition("TEST", null, List.of(field));

        assertThat(structure.getField("UNKNOWN")).isEmpty();
    }

    @Test
    void shouldThrowNullPointerException_whenStructureNameIsNull() {
        final var fields = List.of(
                new StructureField("F1", ElementaryDataType.CHAR, 10, null, null));
        assertThatThrownBy(() -> new StructureDefinition(null, null, fields))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("structureName");
    }

    @Test
    void shouldThrowNullPointerException_whenFieldsIsNull() {
        assertThatThrownBy(() -> new StructureDefinition("TEST", null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("fields");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenStructureNameIsBlank() {
        final var fields = List.of(
                new StructureField("F1", ElementaryDataType.CHAR, 10, null, null));
        assertThatThrownBy(() -> new StructureDefinition("  ", null, fields))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("structureName");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenFieldsIsEmpty() {
        assertThatThrownBy(() -> new StructureDefinition("TEST", null, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fields");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenDuplicateFieldNames() {
        final var fields = List.of(
                new StructureField("F1", ElementaryDataType.CHAR, 10, null, null),
                new StructureField("F1", ElementaryDataType.INT4, null, null, null)
        );
        assertThatThrownBy(() -> new StructureDefinition("TEST", null, fields))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicate field name");
    }

    @Test
    void shouldReturnUnmodifiableFieldsMap() {
        final var field = new StructureField("F1", ElementaryDataType.CHAR, 10, null, null);
        final var structure = new StructureDefinition("TEST", null, List.of(field));

        assertThatThrownBy(() -> structure.getFields().put("NEW", field))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenGetFieldNameIsNull() {
        final var field = new StructureField("F1", ElementaryDataType.CHAR, 10, null, null);
        final var structure = new StructureDefinition("TEST", null, List.of(field));

        assertThatThrownBy(() -> structure.getField(null))
                .isInstanceOf(NullPointerException.class);
    }
}
