package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateTableDefinitionRequest;
import com.erp.kernel.ddic.dto.TableDefinitionDto;
import com.erp.kernel.ddic.model.SchemaLevel;
import com.erp.kernel.ddic.service.TableDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing DDIC table definitions.
 *
 * <p>Provides CRUD operations for table definitions at {@code /api/v1/ddic/tables}.
 * Supports filtering by ANSI/SPARC schema level.
 */
@RestController
@RequestMapping("/api/v1/ddic/tables")
public class TableDefinitionController {

    private final TableDefinitionService tableDefinitionService;

    /**
     * Creates a new table definition controller.
     *
     * @param tableDefinitionService the table definition service
     */
    public TableDefinitionController(final TableDefinitionService tableDefinitionService) {
        this.tableDefinitionService = Objects.requireNonNull(tableDefinitionService,
                "tableDefinitionService must not be null");
    }

    /**
     * Creates a new table definition.
     *
     * @param request the creation request
     * @return the created table definition with HTTP 201
     */
    @PostMapping
    public ResponseEntity<TableDefinitionDto> create(
            @Valid @RequestBody final CreateTableDefinitionRequest request) {
        final var created = tableDefinitionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a table definition by its ID.
     *
     * @param id the table definition ID
     * @return the table definition DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<TableDefinitionDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(tableDefinitionService.findById(id));
    }

    /**
     * Retrieves all table definitions, optionally filtered by schema level.
     *
     * @param schemaLevel optional schema level filter
     * @return the list of matching table definitions
     */
    @GetMapping
    public ResponseEntity<List<TableDefinitionDto>> findAll(
            @RequestParam(required = false) final SchemaLevel schemaLevel) {
        if (schemaLevel != null) {
            return ResponseEntity.ok(tableDefinitionService.findBySchemaLevel(schemaLevel));
        }
        return ResponseEntity.ok(tableDefinitionService.findAll());
    }

    /**
     * Updates an existing table definition.
     *
     * @param id      the table definition ID
     * @param request the update request
     * @return the updated table definition
     */
    @PutMapping("/{id}")
    public ResponseEntity<TableDefinitionDto> update(
            @PathVariable final Long id,
            @Valid @RequestBody final CreateTableDefinitionRequest request) {
        return ResponseEntity.ok(tableDefinitionService.update(id, request));
    }

    /**
     * Deletes a table definition by its ID.
     *
     * @param id the table definition ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        tableDefinitionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
