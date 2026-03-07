package com.erp.kernel.ddic.repository;

import com.erp.kernel.ddic.entity.SearchHelp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link SearchHelp} entities.
 */
public interface SearchHelpRepository extends JpaRepository<SearchHelp, Long> {

    /**
     * Finds a search help by its unique name.
     *
     * @param searchHelpName the search help name to search for
     * @return an {@link Optional} containing the search help, or empty if not found
     */
    Optional<SearchHelp> findBySearchHelpName(String searchHelpName);

    /**
     * Checks whether a search help with the given name exists.
     *
     * @param searchHelpName the search help name to check
     * @return {@code true} if a search help with this name exists
     */
    boolean existsBySearchHelpName(String searchHelpName);
}
