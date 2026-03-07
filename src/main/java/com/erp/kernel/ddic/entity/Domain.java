package com.erp.kernel.ddic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a DDIC domain defining technical attributes for data types.
 *
 * <p>A domain specifies the data type, maximum length, decimal places, and
 * optional value range constraints. Data elements reference domains to
 * inherit their technical properties.
 */
@Entity
@Table(name = "ddic_domain")
public class Domain extends BaseEntity {

    @Column(name = "domain_name", nullable = false, unique = true, length = 30)
    private String domainName;

    @Column(name = "data_type", nullable = false, length = 20)
    private String dataType;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "decimal_places")
    private Integer decimalPlaces;

    @Column(name = "description")
    private String description;

    /**
     * Returns the domain name.
     *
     * @return the unique domain name
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Sets the domain name.
     *
     * @param domainName the unique domain name (max 30 characters)
     */
    public void setDomainName(final String domainName) {
        this.domainName = domainName;
    }

    /**
     * Returns the data type.
     *
     * @return the data type (e.g., CHAR, NUMC, DEC, INT)
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the data type.
     *
     * @param dataType the data type
     */
    public void setDataType(final String dataType) {
        this.dataType = dataType;
    }

    /**
     * Returns the maximum length.
     *
     * @return the maximum length, or {@code null} if unlimited
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the maximum length.
     *
     * @param maxLength the maximum length
     */
    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Returns the number of decimal places.
     *
     * @return the decimal places
     */
    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    /**
     * Sets the number of decimal places.
     *
     * @param decimalPlaces the decimal places
     */
    public void setDecimalPlaces(final Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
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
     * @param description the description (max 255 characters)
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
