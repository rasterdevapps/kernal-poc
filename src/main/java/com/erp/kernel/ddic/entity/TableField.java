package com.erp.kernel.ddic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Represents a field (column) within a DDIC table definition.
 *
 * <p>Each field references a {@link DataElement} for its semantic attributes
 * and belongs to a {@link TableDefinition}. Extension fields (prefixed with "Z_")
 * support client-specific customisations without altering the core schema.
 */
@Entity
@Table(name = "ddic_table_field",
        uniqueConstraints = @UniqueConstraint(columnNames = {"table_definition_id", "field_name"}))
public class TableField extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_definition_id", nullable = false)
    private TableDefinition tableDefinition;

    @Column(name = "field_name", nullable = false, length = 30)
    private String fieldName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_element_id", nullable = false)
    private DataElement dataElement;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "is_key", nullable = false)
    private boolean key;

    @Column(name = "is_nullable", nullable = false)
    private boolean nullable;

    @Column(name = "is_extension", nullable = false)
    private boolean extension;

    /**
     * Returns the parent table definition.
     *
     * @return the table definition
     */
    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    /**
     * Sets the parent table definition.
     *
     * @param tableDefinition the table definition
     */
    public void setTableDefinition(final TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    /**
     * Returns the field name.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name.
     *
     * @param fieldName the field name
     */
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns the data element.
     *
     * @return the data element
     */
    public DataElement getDataElement() {
        return dataElement;
    }

    /**
     * Sets the data element.
     *
     * @param dataElement the data element
     */
    public void setDataElement(final DataElement dataElement) {
        this.dataElement = dataElement;
    }

    /**
     * Returns the position of this field within the table.
     *
     * @return the ordinal position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of this field within the table.
     *
     * @param position the ordinal position
     */
    public void setPosition(final int position) {
        this.position = position;
    }

    /**
     * Returns whether this field is a key field.
     *
     * @return {@code true} if this is a key field
     */
    public boolean isKey() {
        return key;
    }

    /**
     * Sets whether this field is a key field.
     *
     * @param key {@code true} if this is a key field
     */
    public void setKey(final boolean key) {
        this.key = key;
    }

    /**
     * Returns whether this field is nullable.
     *
     * @return {@code true} if the field allows null values
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Sets whether this field is nullable.
     *
     * @param nullable {@code true} if the field allows null values
     */
    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Returns whether this field is a client extension ("Z" field).
     *
     * @return {@code true} if this is an extension field
     */
    public boolean isExtension() {
        return extension;
    }

    /**
     * Sets whether this field is a client extension ("Z" field).
     *
     * @param extension {@code true} if this is an extension field
     */
    public void setExtension(final boolean extension) {
        this.extension = extension;
    }
}
