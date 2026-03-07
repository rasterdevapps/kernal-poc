package com.erp.kernel.numberrange.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * JPA entity representing a number range object.
 *
 * <p>A number range defines a named sequence used for generating unique identifiers
 * (e.g., document numbers, order numbers). It supports both buffered and non-buffered
 * assignment modes, similar to SAP's SNRO transaction.
 */
@Entity
@Table(name = "number_range")
public class NumberRange extends BaseEntity {

    @Column(name = "range_object", nullable = false, unique = true, length = 30)
    private String rangeObject;

    @Column(name = "description")
    private String description;

    @Column(name = "is_buffered", nullable = false)
    private boolean buffered;

    @Column(name = "buffer_size")
    private Integer bufferSize;

    /**
     * Returns the number range object name.
     *
     * @return the range object name
     */
    public String getRangeObject() {
        return rangeObject;
    }

    /**
     * Sets the number range object name.
     *
     * @param rangeObject the range object name (max 30 characters)
     */
    public void setRangeObject(final String rangeObject) {
        this.rangeObject = rangeObject;
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
     * Returns whether this number range uses buffered assignment.
     *
     * @return {@code true} if buffered
     */
    public boolean isBuffered() {
        return buffered;
    }

    /**
     * Sets whether this number range uses buffered assignment.
     *
     * @param buffered {@code true} for buffered mode
     */
    public void setBuffered(final boolean buffered) {
        this.buffered = buffered;
    }

    /**
     * Returns the buffer size for buffered ranges.
     *
     * @return the buffer size, or {@code null} for non-buffered ranges
     */
    public Integer getBufferSize() {
        return bufferSize;
    }

    /**
     * Sets the buffer size.
     *
     * @param bufferSize the buffer size
     */
    public void setBufferSize(final Integer bufferSize) {
        this.bufferSize = bufferSize;
    }
}
