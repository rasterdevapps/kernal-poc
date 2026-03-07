package com.erp.kernel.ddic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a DDIC search help for field value lookups.
 *
 * <p>A search help defines a lookup mechanism for a specific table,
 * similar to SAP's search help functionality in SE11.
 */
@Entity
@Table(name = "ddic_search_help")
public class SearchHelp extends BaseEntity {

    @Column(name = "search_help_name", nullable = false, unique = true, length = 30)
    private String searchHelpName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_definition_id", nullable = false)
    private TableDefinition tableDefinition;

    @Column(name = "description")
    private String description;

    /**
     * Returns the search help name.
     *
     * @return the unique search help name
     */
    public String getSearchHelpName() {
        return searchHelpName;
    }

    /**
     * Sets the search help name.
     *
     * @param searchHelpName the unique search help name
     */
    public void setSearchHelpName(final String searchHelpName) {
        this.searchHelpName = searchHelpName;
    }

    /**
     * Returns the referenced table definition.
     *
     * @return the table definition
     */
    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    /**
     * Sets the referenced table definition.
     *
     * @param tableDefinition the table definition
     */
    public void setTableDefinition(final TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
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
}
