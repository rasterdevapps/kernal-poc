package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateDomainRequest;
import com.erp.kernel.ddic.dto.DomainDto;
import com.erp.kernel.ddic.service.DomainService;
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
 * REST controller for managing DDIC domains.
 *
 * <p>Provides CRUD operations for domains at {@code /api/v1/ddic/domains}.
 */
@RestController
@RequestMapping("/api/v1/ddic/domains")
public class DomainController {

    private final DomainService domainService;

    /**
     * Creates a new domain controller.
     *
     * @param domainService the domain service
     */
    public DomainController(final DomainService domainService) {
        this.domainService = Objects.requireNonNull(domainService, "domainService must not be null");
    }

    /**
     * Creates a new domain.
     *
     * @param request the creation request
     * @return the created domain with HTTP 201
     */
    @PostMapping
    public ResponseEntity<DomainDto> create(@Valid @RequestBody final CreateDomainRequest request) {
        final var created = domainService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a domain by its ID.
     *
     * @param id the domain ID
     * @return the domain DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<DomainDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(domainService.findById(id));
    }

    /**
     * Retrieves all domains.
     *
     * @return the list of all domains
     */
    @GetMapping
    public ResponseEntity<List<DomainDto>> findAll() {
        return ResponseEntity.ok(domainService.findAll());
    }

    /**
     * Updates an existing domain.
     *
     * @param id      the domain ID
     * @param request the update request
     * @return the updated domain
     */
    @PutMapping("/{id}")
    public ResponseEntity<DomainDto> update(@PathVariable final Long id,
                                            @Valid @RequestBody final CreateDomainRequest request) {
        return ResponseEntity.ok(domainService.update(id, request));
    }

    /**
     * Deletes a domain by its ID.
     *
     * @param id the domain ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        domainService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
