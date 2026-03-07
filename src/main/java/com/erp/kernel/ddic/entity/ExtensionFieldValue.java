package com.erp.kernel.ddic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Stores client-specific extension field values using the EAV (Entity-Attribute-Value) pattern.
 *
 * <p>Extension fields allow clients to add custom "Z" fields to standard tables
 * without altering the core database schema. Each record stores a single field value
 * for a specific table row.
 */
@Entity
@Table(name = "ddic_extension_field_value",
        uniqueConstraints = @UniqueConstraint(columnNames = {"table_name", "record_id", "field_name"}))
public class ExtensionFieldValue extends BaseEntity {

    @Column(name = "table_name", nullable = false, length = 30)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "field_name", nullable = false, length = 30)
    private String fieldName;

    @Column(name = "field_value", length = 1024)
    private String fieldValue;

    /**
     * Returns the table name.
     *
     * @return the name of the table this extension belongs to
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the table name.
     *
     * @param tableName the table name
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns the record ID.
     *
     * @return the ID of the record this extension value belongs to
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * Sets the record ID.
     *
     * @param recordId the record ID
     */
    public void setRecordId(final Long recordId) {
        this.recordId = recordId;
    }

    /**
     * Returns the field name.
     *
     * @return the extension field name (must start with "Z_")
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name.
     *
     * @param fieldName the extension field name
     */
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns the field value.
     *
     * @return the stored value for this extension field
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * Sets the field value.
     *
     * @param fieldValue the value to store
     */
    public void setFieldValue(final String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
