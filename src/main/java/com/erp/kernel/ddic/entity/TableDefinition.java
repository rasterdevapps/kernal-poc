package com.erp.kernel.ddic.entity;

import com.erp.kernel.ddic.model.SchemaLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * Represents a DDIC table definition within the ANSI/SPARC three-schema architecture.
 *
 * <p>Each table definition is assigned a {@link SchemaLevel} indicating whether it
 * represents an external view, a conceptual model, or an internal storage structure.
 */
@Entity
@Table(name = "ddic_table_definition")
public class TableDefinition extends BaseEntity {

    @Column(name = "table_name", nullable = false, unique = true, length = 30)
    private String tableName;

    @Enumerated(EnumType.STRING)
    @Column(name = "schema_level", nullable = false, length = 20)
    private SchemaLevel schemaLevel;

    @Column(name = "description")
    private String description;

    @Column(name = "is_client_specific", nullable = false)
    private boolean clientSpecific;

    /**
     * Returns the table name.
     *
     * @return the unique table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the table name.
     *
     * @param tableName the unique table name
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns the schema level.
     *
     * @return the ANSI/SPARC schema level
     */
    public SchemaLevel getSchemaLevel() {
        return schemaLevel;
    }

    /**
     * Sets the schema level.
     *
     * @param schemaLevel the ANSI/SPARC schema level
     */
    public void setSchemaLevel(final SchemaLevel schemaLevel) {
        this.schemaLevel = schemaLevel;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns whether this table is client-specific.
     *
     * @return {@code true} if client-specific
     */
    public boolean isClientSpecific() {
        return clientSpecific;
    }

    /**
     * Sets whether this table is client-specific.
     *
     * @param clientSpecific {@code true} if client-specific
     */
    public void setClientSpecific(final boolean clientSpecific) {
        this.clientSpecific = clientSpecific;
    }
}
