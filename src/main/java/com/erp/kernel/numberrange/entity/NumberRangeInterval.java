package com.erp.kernel.numberrange.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA entity representing a number range interval.
 *
 * <p>An interval defines a contiguous range of numbers within a {@link NumberRange},
 * identified by a two-character sub-object code. The current number tracks the last
 * assigned value for sequential assignment.
 */
@Entity
@Table(name = "number_range_interval")
public class NumberRangeInterval extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "number_range_id", nullable = false)
    private NumberRange numberRange;

    @Column(name = "sub_object", nullable = false, length = 2)
    private String subObject;

    @Column(name = "from_number", nullable = false, length = 20)
    private String fromNumber;

    @Column(name = "to_number", nullable = false, length = 20)
    private String toNumber;

    @Column(name = "current_number", nullable = false, length = 20)
    private String currentNumber;

    /**
     * Returns the parent number range.
     *
     * @return the number range
     */
    public NumberRange getNumberRange() {
        return numberRange;
    }

    /**
     * Sets the parent number range.
     *
     * @param numberRange the number range
     */
    public void setNumberRange(final NumberRange numberRange) {
        this.numberRange = numberRange;
    }

    /**
     * Returns the sub-object identifier.
     *
     * @return the sub-object code (2 characters)
     */
    public String getSubObject() {
        return subObject;
    }

    /**
     * Sets the sub-object identifier.
     *
     * @param subObject the sub-object code
     */
    public void setSubObject(final String subObject) {
        this.subObject = subObject;
    }

    /**
     * Returns the lower bound of the interval.
     *
     * @return the from number
     */
    public String getFromNumber() {
        return fromNumber;
    }

    /**
     * Sets the lower bound of the interval.
     *
     * @param fromNumber the from number
     */
    public void setFromNumber(final String fromNumber) {
        this.fromNumber = fromNumber;
    }

    /**
     * Returns the upper bound of the interval.
     *
     * @return the to number
     */
    public String getToNumber() {
        return toNumber;
    }

    /**
     * Sets the upper bound of the interval.
     *
     * @param toNumber the to number
     */
    public void setToNumber(final String toNumber) {
        this.toNumber = toNumber;
    }

    /**
     * Returns the current (last assigned) number.
     *
     * @return the current number
     */
    public String getCurrentNumber() {
        return currentNumber;
    }

    /**
     * Sets the current number.
     *
     * @param currentNumber the current number
     */
    public void setCurrentNumber(final String currentNumber) {
        this.currentNumber = currentNumber;
    }
}
