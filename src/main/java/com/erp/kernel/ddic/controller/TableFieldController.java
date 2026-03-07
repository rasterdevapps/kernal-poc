package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateTableFieldRequest;
import com.erp.kernel.ddic.dto.TableFieldDto;
import com.erp.kernel.ddic.service.TableFieldService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing DDIC table fields.
 *
 * <p>Provides operations for table fields at {@code /api/v1/ddic/fields}.
 * Supports querying by table definition and filtering extension fields.
 */
@RestController
@RequestMapping("/api/v1/ddic/fields")
public class TableFieldController {

    private final TableFieldService tableFieldService;

    /**
     * Creates a new table field controller.
     *
     * @param tableFieldService the table field service
     */
    public TableFieldController(final TableFieldService tableFieldService) {
        this.tableFieldService = Objects.requireNonNull(tableFieldService,
                "tableFieldService must not be null");
    }

    /**
     * Creates a new table field.
     *
     * @param request the creation request
     * @return the created table field with HTTP 201
     */
    @PostMapping
    public ResponseEntity<TableFieldDto> create(
            @Valid @RequestBody final CreateTableFieldRequest request) {
        final var created = tableFieldService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a table field by its ID.
     *
     * @param id the table field ID
     * @return the table field DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<TableFieldDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(tableFieldService.findById(id));
    }

    /**
     * Retrieves all fields for a specific table definition.
     *
     * @param tableDefinitionId the table definition ID
     * @param extensionsOnly    if true, returns only extension fields
     * @return the list of table field DTOs
     */
    @GetMapping
    public ResponseEntity<List<TableFieldDto>> findByTableDefinition(
            @RequestParam final Long tableDefinitionId,
            @RequestParam(defaultValue = "false") final boolean extensionsOnly) {
        if (extensionsOnly) {
            return ResponseEntity.ok(tableFieldService.findExtensionFields(tableDefinitionId));
        }
        return ResponseEntity.ok(tableFieldService.findByTableDefinitionId(tableDefinitionId));
    }

    /**
     * Deletes a table field by its ID.
     *
     * @param id the table field ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        tableFieldService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
