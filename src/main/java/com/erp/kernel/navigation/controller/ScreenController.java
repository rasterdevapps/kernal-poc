package com.erp.kernel.navigation.controller;

import com.erp.kernel.navigation.dto.CreateScreenRequest;
import com.erp.kernel.navigation.dto.ScreenDto;
import com.erp.kernel.navigation.service.ScreenService;
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
 * REST controller for managing screens.
 *
 * <p>Provides CRUD operations and module-based queries for screens at
 * {@code /api/v1/navigation/screens}, enabling management of presentation
 * layer screen definitions linked to transaction codes.
 */
@RestController
@RequestMapping("/api/v1/navigation/screens")
public class ScreenController {

    private final ScreenService screenService;

    /**
     * Creates a new screen controller.
     *
     * @param screenService the screen service
     */
    public ScreenController(final ScreenService screenService) {
        this.screenService = Objects.requireNonNull(screenService, "screenService must not be null");
    }

    /**
     * Creates a new screen.
     *
     * @param request the creation request
     * @return the created screen with HTTP 201
     */
    @PostMapping
    public ResponseEntity<ScreenDto> create(@Valid @RequestBody final CreateScreenRequest request) {
        final var created = screenService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a screen by ID.
     *
     * @param id the screen ID
     * @return the screen DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScreenDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(screenService.findById(id));
    }

    /**
     * Retrieves all screens.
     *
     * @return the list of all screens
     */
    @GetMapping
    public ResponseEntity<List<ScreenDto>> findAll() {
        return ResponseEntity.ok(screenService.findAll());
    }

    /**
     * Retrieves all screens belonging to a specific module.
     *
     * @param module the module name
     * @return the list of screens in the module
     */
    @GetMapping("/module/{module}")
    public ResponseEntity<List<ScreenDto>> findByModule(@PathVariable final String module) {
        return ResponseEntity.ok(screenService.findByModule(module));
    }

    /**
     * Updates an existing screen.
     *
     * @param id      the screen ID
     * @param request the update request
     * @return the updated screen
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScreenDto> update(@PathVariable final Long id,
                                            @Valid @RequestBody final CreateScreenRequest request) {
        return ResponseEntity.ok(screenService.update(id, request));
    }

    /**
     * Deletes a screen by ID.
     *
     * @param id the screen ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        screenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
