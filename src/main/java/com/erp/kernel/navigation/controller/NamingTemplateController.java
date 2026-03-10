package com.erp.kernel.navigation.controller;

import com.erp.kernel.navigation.dto.CreateNamingTemplateRequest;
import com.erp.kernel.navigation.dto.NamingTemplateDto;
import com.erp.kernel.navigation.service.NamingTemplateService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing naming templates.
 *
 * <p>Provides CRUD operations for naming templates at
 * {@code /api/v1/navigation/naming-templates}, enabling management of
 * standardised entity identifier patterns used across the ERP system.
 */
@RestController
@RequestMapping("/api/v1/navigation/naming-templates")
public class NamingTemplateController {

    private final NamingTemplateService namingTemplateService;

    /**
     * Creates a new naming template controller.
     *
     * @param namingTemplateService the naming template service
     */
    public NamingTemplateController(final NamingTemplateService namingTemplateService) {
        this.namingTemplateService = Objects.requireNonNull(namingTemplateService,
                "namingTemplateService must not be null");
    }

    /**
     * Creates a new naming template.
     *
     * @param request the creation request
     * @return the created naming template with HTTP 201
     */
    @PostMapping
    public ResponseEntity<NamingTemplateDto> create(
            @Valid @RequestBody final CreateNamingTemplateRequest request) {
        final var created = namingTemplateService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a naming template by ID.
     *
     * @param id the naming template ID
     * @return the naming template DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<NamingTemplateDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(namingTemplateService.findById(id));
    }

    /**
     * Retrieves all naming templates.
     *
     * @return the list of all naming templates
     */
    @GetMapping
    public ResponseEntity<List<NamingTemplateDto>> findAll() {
        return ResponseEntity.ok(namingTemplateService.findAll());
    }

    /**
     * Updates an existing naming template.
     *
     * @param id      the naming template ID
     * @param request the update request
     * @return the updated naming template
     */
    @PutMapping("/{id}")
    public ResponseEntity<NamingTemplateDto> update(
            @PathVariable final Long id,
            @Valid @RequestBody final CreateNamingTemplateRequest request) {
        return ResponseEntity.ok(namingTemplateService.update(id, request));
    }

    /**
     * Deletes a naming template by ID.
     *
     * @param id the naming template ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        namingTemplateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
