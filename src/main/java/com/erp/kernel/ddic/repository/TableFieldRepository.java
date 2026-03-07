package com.erp.kernel.ddic.repository;

import com.erp.kernel.ddic.entity.TableField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link TableField} entities.
 */
public interface TableFieldRepository extends JpaRepository<TableField, Long> {

    /**
     * Finds all fields belonging to a specific table definition.
     *
     * @param tableDefinitionId the table definition ID
     * @return the list of fields ordered by position
     */
    List<TableField> findByTableDefinitionIdOrderByPositionAsc(Long tableDefinitionId);

    /**
     * Finds all extension fields belonging to a specific table definition.
     *
     * @param tableDefinitionId the table definition ID
     * @return the list of extension fields
     */
    List<TableField> findByTableDefinitionIdAndExtensionTrue(Long tableDefinitionId);

    /**
     * Checks whether a field with the given name exists in the specified table.
     *
     * @param tableDefinitionId the table definition ID
     * @param fieldName the field name to check
     * @return {@code true} if the field exists
     */
    boolean existsByTableDefinitionIdAndFieldName(Long tableDefinitionId, String fieldName);
}
