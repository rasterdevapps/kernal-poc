package com.erp.kernel.ddic.repository;

import com.erp.kernel.ddic.entity.DataElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link DataElement} entities.
 */
public interface DataElementRepository extends JpaRepository<DataElement, Long> {

    /**
     * Finds a data element by its unique name.
     *
     * @param elementName the element name to search for
     * @return an {@link Optional} containing the data element, or empty if not found
     */
    Optional<DataElement> findByElementName(String elementName);

    /**
     * Checks whether a data element with the given name exists.
     *
     * @param elementName the element name to check
     * @return {@code true} if a data element with this name exists
     */
    boolean existsByElementName(String elementName);
}
