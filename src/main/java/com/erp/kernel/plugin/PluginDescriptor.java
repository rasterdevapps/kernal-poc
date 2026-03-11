package com.erp.kernel.plugin;

import java.util.List;
import java.util.Objects;

/**
 * Immutable metadata about a registered plugin.
 *
 * <p>Captures the plugin's identity, version, description, dependencies,
 * and current lifecycle state.
 *
 * @param id           the unique plugin identifier
 * @param name         the human-readable plugin name
 * @param version      the plugin version
 * @param description  a brief description of the plugin's purpose
 * @param dependencies the identifiers of required dependency plugins
 * @param state        the current lifecycle state
 */
public record PluginDescriptor(
        String id,
        String name,
        String version,
        String description,
        List<String> dependencies,
        PluginState state
) {

    /**
     * Creates a validated plugin descriptor.
     *
     * @param id           the plugin identifier (required)
     * @param name         the plugin name (required)
     * @param version      the plugin version (required)
     * @param description  the description (required)
     * @param dependencies the dependency list (required, may be empty)
     * @param state        the plugin state (required)
     */
    public PluginDescriptor {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(version, "version must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(dependencies, "dependencies must not be null");
        Objects.requireNonNull(state, "state must not be null");
        dependencies = List.copyOf(dependencies);
    }

    /**
     * Creates a descriptor from a plugin instance.
     *
     * @param plugin      the plugin
     * @param description the plugin description
     * @return a new plugin descriptor
     */
    public static PluginDescriptor from(final Plugin plugin, final String description) {
        Objects.requireNonNull(plugin, "plugin must not be null");
        Objects.requireNonNull(description, "description must not be null");
        return new PluginDescriptor(
                plugin.getId(),
                plugin.getName(),
                plugin.getVersion(),
                description,
                plugin.getDependencies(),
                plugin.getState());
    }
}
