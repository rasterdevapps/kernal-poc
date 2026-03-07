package com.erp.kernel.ddic.repository;

import com.erp.kernel.ddic.entity.ExtensionFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link ExtensionFieldValue} entities.
 */
public interface ExtensionFieldValueRepository extends JpaRepository<ExtensionFieldValue, Long> {

    /**
     * Finds all extension field values for a specific record.
     *
     * @param tableName the table name
     * @param recordId  the record ID
     * @return the list of extension field values
     */
    List<ExtensionFieldValue> findByTableNameAndRecordId(String tableName, Long recordId);

    /**
     * Finds a specific extension field value.
     *
     * @param tableName the table name
     * @param recordId  the record ID
     * @param fieldName the field name
     * @return an {@link Optional} containing the value, or empty if not found
     */
    Optional<ExtensionFieldValue> findByTableNameAndRecordIdAndFieldName(
            String tableName, Long recordId, String fieldName);
}
