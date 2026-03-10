package com.erp.kernel.navigation.repository;

import com.erp.kernel.navigation.entity.TCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link TCode} entity persistence operations.
 */
public interface TCodeRepository extends JpaRepository<TCode, Long> {

    /**
     * Checks whether a T-code exists with the given code.
     *
     * @param code the T-code
     * @return {@code true} if a T-code with the code exists
     */
    boolean existsByCode(String code);

    /**
     * Finds a T-code by code.
     *
     * @param code the T-code
     * @return the T-code if found
     */
    Optional<TCode> findByCode(String code);

    /**
     * Finds all T-codes belonging to a specific module.
     *
     * @param module the module name
     * @return the list of T-codes in the module
     */
    List<TCode> findByModule(String module);

    /**
     * Finds all active T-codes.
     *
     * @return the list of active T-codes
     */
    List<TCode> findByActiveTrue();
}
