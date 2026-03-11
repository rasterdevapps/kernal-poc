package com.erp.kernel.plugin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Objects;

/**
 * REST controller for querying registered plugins.
 *
 * <p>Provides read-only endpoints for listing and inspecting plugins
 * at {@code /api/v1/plugins}.
 */
@RestController
@RequestMapping("/api/v1/plugins")
public class PluginController {

    private final PluginRegistry pluginRegistry;

    /**
     * Creates a new plugin controller.
     *
     * @param pluginRegistry the plugin registry
     */
    public PluginController(final PluginRegistry pluginRegistry) {
        this.pluginRegistry = Objects.requireNonNull(
                pluginRegistry, "pluginRegistry must not be null");
    }

    /**
     * Returns all registered plugins.
     *
     * @return the registered plugins
     */
    @GetMapping
    public ResponseEntity<Collection<Plugin>> getAll() {
        return ResponseEntity.ok(pluginRegistry.getAll());
    }

    /**
     * Returns the plugin with the given identifier.
     *
     * @param id the plugin identifier
     * @return the plugin, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plugin> findById(@PathVariable final String id) {
        return pluginRegistry.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
