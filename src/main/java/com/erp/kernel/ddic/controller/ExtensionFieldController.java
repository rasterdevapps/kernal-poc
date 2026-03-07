package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateExtensionFieldValueRequest;
import com.erp.kernel.ddic.dto.ExtensionFieldValueDto;
import com.erp.kernel.ddic.service.ExtensionFieldService;
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
 * REST controller for managing client-specific extension field values.
 *
 * <p>Provides operations for "Z" field values at {@code /api/v1/ddic/extensions}.
 */
@RestController
@RequestMapping("/api/v1/ddic/extensions")
public class ExtensionFieldController {

    private final ExtensionFieldService extensionFieldService;

    /**
     * Creates a new extension field controller.
     *
     * @param extensionFieldService the extension field service
     */
    public ExtensionFieldController(final ExtensionFieldService extensionFieldService) {
        this.extensionFieldService = Objects.requireNonNull(extensionFieldService,
                "extensionFieldService must not be null");
    }

    /**
     * Creates or updates an extension field value.
     *
     * @param request the creation request
     * @return the created or updated extension field value with HTTP 201
     */
    @PostMapping
    public ResponseEntity<ExtensionFieldValueDto> save(
            @Valid @RequestBody final CreateExtensionFieldValueRequest request) {
        final var saved = extensionFieldService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Retrieves all extension field values for a specific record.
     *
     * @param tableName the table name
     * @param recordId  the record ID
     * @return the list of extension field values
     */
    @GetMapping
    public ResponseEntity<List<ExtensionFieldValueDto>> findByRecord(
            @RequestParam final String tableName,
            @RequestParam final Long recordId) {
        return ResponseEntity.ok(extensionFieldService.findByRecord(tableName, recordId));
    }

    /**
     * Deletes an extension field value by its ID.
     *
     * @param id the extension field value ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        extensionFieldService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
