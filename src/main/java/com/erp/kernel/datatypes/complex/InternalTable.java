package com.erp.kernel.datatypes.complex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an internal table — an ordered, in-memory collection of structured rows.
 *
 * <p>An internal table is analogous to an ABAP internal table. Each row conforms
 * to the associated {@link StructureDefinition}. Rows are stored as ordered maps
 * of field name to string value.
 */
public final class InternalTable {

    private final StructureDefinition structure;
    private final List<Map<String, String>> rows;

    /**
     * Creates a new, empty internal table with the given structure.
     *
     * @param structure the structure definition that each row must conform to
     */
    public InternalTable(final StructureDefinition structure) {
        this.structure = Objects.requireNonNull(structure, "structure must not be null");
        this.rows = new ArrayList<>();
    }

    /**
     * Returns the structure definition for this table.
     *
     * @return the structure definition
     */
    public StructureDefinition getStructure() {
        return structure;
    }

    /**
     * Appends a row to the table.
     *
     * <p>The row must contain only keys that match defined field names.
     *
     * @param row the row data as field name to value map
     * @throws IllegalArgumentException if the row contains unknown field names
     */
    public void appendRow(final Map<String, String> row) {
        Objects.requireNonNull(row, "row must not be null");
        for (final var key : row.keySet()) {
            if (structure.getField(key).isEmpty()) {
                throw new IllegalArgumentException(
                        "Unknown field '%s' in structure '%s'"
                                .formatted(key, structure.getStructureName()));
            }
        }
        rows.add(new LinkedHashMap<>(row));
    }

    /**
     * Returns an unmodifiable view of all rows.
     *
     * @return the list of rows
     */
    public List<Map<String, String>> getRows() {
        return Collections.unmodifiableList(rows);
    }

    /**
     * Returns the number of rows in the table.
     *
     * @return the row count
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Clears all rows from the table.
     */
    public void clear() {
        rows.clear();
    }
}
