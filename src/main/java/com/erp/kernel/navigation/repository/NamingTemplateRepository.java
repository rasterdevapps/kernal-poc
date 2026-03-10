package com.erp.kernel.navigation.repository;

import com.erp.kernel.navigation.entity.NamingTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link NamingTemplate} entity persistence operations.
 */
public interface NamingTemplateRepository extends JpaRepository<NamingTemplate, Long> {

    /**
     * Checks whether a naming template exists for the given entity type.
     *
     * @param entityType the entity type
     * @return {@code true} if a template for the entity type exists
     */
    boolean existsByEntityType(String entityType);

    /**
     * Finds a naming template by entity type.
     *
     * @param entityType the entity type
     * @return the naming template if found
     */
    Optional<NamingTemplate> findByEntityType(String entityType);
}
