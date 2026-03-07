package com.erp.kernel.ddic.repository;

import com.erp.kernel.ddic.entity.TableDefinition;
import com.erp.kernel.ddic.model.SchemaLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link TableDefinition} entities.
 */
public interface TableDefinitionRepository extends JpaRepository<TableDefinition, Long> {

    /**
     * Finds a table definition by its unique name.
     *
     * @param tableName the table name to search for
     * @return an {@link Optional} containing the table definition, or empty if not found
     */
    Optional<TableDefinition> findByTableName(String tableName);

    /**
     * Checks whether a table definition with the given name exists.
     *
     * @param tableName the table name to check
     * @return {@code true} if a table definition with this name exists
     */
    boolean existsByTableName(String tableName);

    /**
     * Finds all table definitions with the given schema level.
     *
     * @param schemaLevel the schema level to filter by
     * @return the list of matching table definitions
     */
    List<TableDefinition> findBySchemaLevel(SchemaLevel schemaLevel);
}
