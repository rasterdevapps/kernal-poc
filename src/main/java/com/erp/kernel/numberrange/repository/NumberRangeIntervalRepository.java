package com.erp.kernel.numberrange.repository;

import com.erp.kernel.numberrange.entity.NumberRange;
import com.erp.kernel.numberrange.entity.NumberRangeInterval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link NumberRangeInterval} entities.
 */
public interface NumberRangeIntervalRepository extends JpaRepository<NumberRangeInterval, Long> {

    /**
     * Finds all intervals belonging to a number range.
     *
     * @param numberRange the parent number range
     * @return the list of intervals
     */
    List<NumberRangeInterval> findByNumberRange(NumberRange numberRange);

    /**
     * Finds an interval by its parent range and sub-object code.
     *
     * @param numberRange the parent number range
     * @param subObject   the sub-object code
     * @return an {@link Optional} containing the interval, or empty if not found
     */
    Optional<NumberRangeInterval> findByNumberRangeAndSubObject(NumberRange numberRange, String subObject);
}
