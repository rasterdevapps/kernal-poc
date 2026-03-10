package com.erp.kernel.navigation.controller;

import com.erp.kernel.navigation.dto.CreateTCodeRequest;
import com.erp.kernel.navigation.dto.TCodeDto;
import com.erp.kernel.navigation.service.TCodeService;
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
 * REST controller for managing transaction codes (T-Codes).
 *
 * <p>Provides CRUD operations and lookup queries for T-Codes at
 * {@code /api/v1/navigation/tcodes}, including searches by code string,
 * module, and active status.
 */
@RestController
@RequestMapping("/api/v1/navigation/tcodes")
public class TCodeController {

    private final TCodeService tCodeService;

    /**
     * Creates a new T-Code controller.
     *
     * @param tCodeService the T-Code service
     */
    public TCodeController(final TCodeService tCodeService) {
        this.tCodeService = Objects.requireNonNull(tCodeService, "tCodeService must not be null");
    }

    /**
     * Creates a new transaction code.
     *
     * @param request the creation request
     * @return the created T-Code with HTTP 201
     */
    @PostMapping
    public ResponseEntity<TCodeDto> create(@Valid @RequestBody final CreateTCodeRequest request) {
        final var created = tCodeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a transaction code by ID.
     *
     * @param id the T-Code ID
     * @return the T-Code DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<TCodeDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(tCodeService.findById(id));
    }

    /**
     * Retrieves all transaction codes.
     *
     * @return the list of all T-Codes
     */
    @GetMapping
    public ResponseEntity<List<TCodeDto>> findAll() {
        return ResponseEntity.ok(tCodeService.findAll());
    }

    /**
     * Retrieves a transaction code by its unique code string.
     *
     * @param code the T-Code code
     * @return the T-Code DTO
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<TCodeDto> findByCode(@PathVariable final String code) {
        return ResponseEntity.ok(tCodeService.findByCode(code));
    }

    /**
     * Retrieves all transaction codes belonging to a specific module.
     *
     * @param module the module name
     * @return the list of T-Codes in the module
     */
    @GetMapping("/module/{module}")
    public ResponseEntity<List<TCodeDto>> findByModule(@PathVariable final String module) {
        return ResponseEntity.ok(tCodeService.findByModule(module));
    }

    /**
     * Retrieves all active transaction codes.
     *
     * @return the list of active T-Codes
     */
    @GetMapping("/active")
    public ResponseEntity<List<TCodeDto>> findAllActive() {
        return ResponseEntity.ok(tCodeService.findAllActive());
    }

    /**
     * Updates an existing transaction code.
     *
     * @param id      the T-Code ID
     * @param request the update request
     * @return the updated T-Code
     */
    @PutMapping("/{id}")
    public ResponseEntity<TCodeDto> update(@PathVariable final Long id,
                                           @Valid @RequestBody final CreateTCodeRequest request) {
        return ResponseEntity.ok(tCodeService.update(id, request));
    }

    /**
     * Deletes a transaction code by ID.
     *
     * @param id the T-Code ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        tCodeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
