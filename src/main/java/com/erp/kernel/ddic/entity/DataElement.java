package com.erp.kernel.ddic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a DDIC data element defining semantic attributes.
 *
 * <p>A data element provides labels and descriptions for fields and references
 * a {@link Domain} for its technical attributes (data type, length, etc.).
 */
@Entity
@Table(name = "ddic_data_element")
public class DataElement extends BaseEntity {

    @Column(name = "element_name", nullable = false, unique = true, length = 30)
    private String elementName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @Column(name = "short_label", length = 10)
    private String shortLabel;

    @Column(name = "medium_label", length = 20)
    private String mediumLabel;

    @Column(name = "long_label", length = 40)
    private String longLabel;

    @Column(name = "description")
    private String description;

    /**
     * Returns the element name.
     *
     * @return the unique element name
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Sets the element name.
     *
     * @param elementName the unique element name
     */
    public void setElementName(final String elementName) {
        this.elementName = elementName;
    }

    /**
     * Returns the referenced domain.
     *
     * @return the domain providing technical attributes
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Sets the referenced domain.
     *
     * @param domain the domain
     */
    public void setDomain(final Domain domain) {
        this.domain = domain;
    }

    /**
     * Returns the short label.
     *
     * @return the short label (max 10 characters)
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * Sets the short label.
     *
     * @param shortLabel the short label
     */
    public void setShortLabel(final String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * Returns the medium label.
     *
     * @return the medium label (max 20 characters)
     */
    public String getMediumLabel() {
        return mediumLabel;
    }

    /**
     * Sets the medium label.
     *
     * @param mediumLabel the medium label
     */
    public void setMediumLabel(final String mediumLabel) {
        this.mediumLabel = mediumLabel;
    }

    /**
     * Returns the long label.
     *
     * @return the long label (max 40 characters)
     */
    public String getLongLabel() {
        return longLabel;
    }

    /**
     * Sets the long label.
     *
     * @param longLabel the long label
     */
    public void setLongLabel(final String longLabel) {
        this.longLabel = longLabel;
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
