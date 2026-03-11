package com.erp.kernel.vertical;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * REST controller for querying vertical modules.
 *
 * <p>Provides read-only endpoints for listing and inspecting vertical
 * modules at {@code /api/v1/verticals}.
 */
@RestController
@RequestMapping("/api/v1/verticals")
public class VerticalController {

    private final VerticalRegistry verticalRegistry;

    /**
     * Creates a new vertical controller.
     *
     * @param verticalRegistry the vertical registry
     */
    public VerticalController(final VerticalRegistry verticalRegistry) {
        this.verticalRegistry = Objects.requireNonNull(
                verticalRegistry, "verticalRegistry must not be null");
    }

    /**
     * Returns all registered vertical modules.
     *
     * @return the registered vertical modules
     */
    @GetMapping
    public ResponseEntity<Collection<VerticalModule>> getAll() {
        return ResponseEntity.ok(verticalRegistry.getAll());
    }

    /**
     * Returns the vertical module with the given identifier.
     *
     * @param id the module identifier
     * @return the vertical module, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<VerticalModule> findById(@PathVariable final String id) {
        return verticalRegistry.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Returns vertical modules filtered by vertical type.
     *
     * @param type the vertical type to filter by
     * @return the matching vertical modules
     */
    @GetMapping("/by-type")
    public ResponseEntity<List<VerticalModule>> findByType(
            @RequestParam final VerticalType type) {
        return ResponseEntity.ok(verticalRegistry.findByVerticalType(type));
    }
}
