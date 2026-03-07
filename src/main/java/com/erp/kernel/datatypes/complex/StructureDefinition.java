package com.erp.kernel.datatypes.complex;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a structured data type composed of multiple {@link StructureField} entries.
 *
 * <p>A structure is an ordered collection of named fields, similar to an ABAP structure
 * or a database row definition. Fields are uniquely identified by name and maintain
 * insertion order.
 */
public final class StructureDefinition {

    private final String structureName;
    private final String description;
    private final Map<String, StructureField> fields;

    /**
     * Creates a new structure definition.
     *
     * @param structureName the structure name (required)
     * @param description   the structure description (may be {@code null})
     * @param fields        the ordered list of fields (required, must not be empty)
     * @throws IllegalArgumentException if fields is empty or contains duplicate field names
     */
    public StructureDefinition(final String structureName,
                               final String description,
                               final List<StructureField> fields) {
        Objects.requireNonNull(structureName, "structureName must not be null");
        Objects.requireNonNull(fields, "fields must not be null");
        if (structureName.isBlank()) {
            throw new IllegalArgumentException("structureName must not be blank");
        }
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("fields must not be empty");
        }
        this.structureName = structureName;
        this.description = description;
        this.fields = new LinkedHashMap<>();
        for (final var field : fields) {
            if (this.fields.containsKey(field.fieldName())) {
                throw new IllegalArgumentException(
                        "Duplicate field name: '%s'".formatted(field.fieldName()));
            }
            this.fields.put(field.fieldName(), field);
        }
    }

    /**
     * Returns the structure name.
     *
     * @return the structure name
     */
    public String getStructureName() {
        return structureName;
    }

    /**
     * Returns the structure description.
     *
     * @return the description, or {@code null} if not set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns an unmodifiable view of the fields in insertion order.
     *
     * @return the ordered map of field name to field definition
     */
    public Map<String, StructureField> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    /**
     * Returns the field with the given name.
     *
     * @param fieldName the field name to look up
     * @return an {@link Optional} containing the field, or empty if not found
     */
    public Optional<StructureField> getField(final String fieldName) {
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        return Optional.ofNullable(fields.get(fieldName));
    }

    /**
     * Returns the number of fields in this structure.
     *
     * @return the field count
     */
    public int getFieldCount() {
        return fields.size();
    }
}
