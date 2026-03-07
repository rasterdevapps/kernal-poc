package com.erp.kernel.datatypes.complex;

import com.erp.kernel.datatypes.elementary.ElementaryDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link InternalTable}.
 */
class InternalTableTest {

    private StructureDefinition structure;
    private InternalTable table;

    @BeforeEach
    void setUp() {
        final var fields = List.of(
                new StructureField("ID", ElementaryDataType.INT4, null, null, "Identifier"),
                new StructureField("NAME", ElementaryDataType.CHAR, 50, null, "Name")
        );
        structure = new StructureDefinition("S_TEST", "Test structure", fields);
        table = new InternalTable(structure);
    }

    @Test
    void shouldCreateEmptyTable() {
        assertThat(table.getRowCount()).isZero();
        assertThat(table.getRows()).isEmpty();
        assertThat(table.getStructure()).isSameAs(structure);
    }

    @Test
    void shouldAppendRow() {
        final var row = new LinkedHashMap<String, String>();
        row.put("ID", "1");
        row.put("NAME", "Test");

        table.appendRow(row);

        assertThat(table.getRowCount()).isEqualTo(1);
        assertThat(table.getRows().getFirst()).containsEntry("ID", "1");
    }

    @Test
    void shouldAppendMultipleRows() {
        table.appendRow(Map.of("ID", "1", "NAME", "First"));
        table.appendRow(Map.of("ID", "2", "NAME", "Second"));

        assertThat(table.getRowCount()).isEqualTo(2);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenRowContainsUnknownField() {
        assertThatThrownBy(() -> table.appendRow(Map.of("UNKNOWN", "value")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown field");
    }

    @Test
    void shouldThrowNullPointerException_whenRowIsNull() {
        assertThatThrownBy(() -> table.appendRow(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldClearAllRows() {
        table.appendRow(Map.of("ID", "1", "NAME", "Test"));
        assertThat(table.getRowCount()).isEqualTo(1);

        table.clear();

        assertThat(table.getRowCount()).isZero();
    }

    @Test
    void shouldReturnUnmodifiableRowsList() {
        assertThatThrownBy(() -> table.getRows().add(Map.of()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenStructureIsNull() {
        assertThatThrownBy(() -> new InternalTable(null))
                .isInstanceOf(NullPointerException.class);
    }
}
