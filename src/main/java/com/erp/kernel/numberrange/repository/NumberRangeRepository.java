package com.erp.kernel.numberrange.repository;

import com.erp.kernel.numberrange.entity.NumberRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link NumberRange} entities.
 */
public interface NumberRangeRepository extends JpaRepository<NumberRange, Long> {

    /**
     * Finds a number range by its unique object name.
     *
     * @param rangeObject the range object name
     * @return an {@link Optional} containing the number range, or empty if not found
     */
    Optional<NumberRange> findByRangeObject(String rangeObject);

    /**
     * Checks whether a number range with the given object name exists.
     *
     * @param rangeObject the range object name
     * @return {@code true} if a range with this name exists
     */
    boolean existsByRangeObject(String rangeObject);
}
