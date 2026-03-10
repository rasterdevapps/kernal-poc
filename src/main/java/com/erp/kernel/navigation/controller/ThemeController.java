package com.erp.kernel.navigation.controller;

import com.erp.kernel.navigation.dto.CreateThemeRequest;
import com.erp.kernel.navigation.dto.ThemeDto;
import com.erp.kernel.navigation.service.ThemeService;
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
 * REST controller for managing UI themes.
 *
 * <p>Provides CRUD operations for themes at {@code /api/v1/navigation/themes},
 * enabling creation, retrieval, update, and deletion of presentation layer
 * theme configurations.
 */
@RestController
@RequestMapping("/api/v1/navigation/themes")
public class ThemeController {

    private final ThemeService themeService;

    /**
     * Creates a new theme controller.
     *
     * @param themeService the theme service
     */
    public ThemeController(final ThemeService themeService) {
        this.themeService = Objects.requireNonNull(themeService, "themeService must not be null");
    }

    /**
     * Creates a new theme.
     *
     * @param request the creation request
     * @return the created theme with HTTP 201
     */
    @PostMapping
    public ResponseEntity<ThemeDto> create(@Valid @RequestBody final CreateThemeRequest request) {
        final var created = themeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a theme by ID.
     *
     * @param id the theme ID
     * @return the theme DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ThemeDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(themeService.findById(id));
    }

    /**
     * Retrieves all themes.
     *
     * @return the list of all themes
     */
    @GetMapping
    public ResponseEntity<List<ThemeDto>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    /**
     * Updates an existing theme.
     *
     * @param id      the theme ID
     * @param request the update request
     * @return the updated theme
     */
    @PutMapping("/{id}")
    public ResponseEntity<ThemeDto> update(@PathVariable final Long id,
                                           @Valid @RequestBody final CreateThemeRequest request) {
        return ResponseEntity.ok(themeService.update(id, request));
    }

    /**
     * Deletes a theme by ID.
     *
     * @param id the theme ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
