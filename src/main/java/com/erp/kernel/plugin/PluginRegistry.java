package com.erp.kernel.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Central registry for managing plugin lifecycle and lookup.
 *
 * <p>Handles plugin registration, initialization, starting, stopping,
 * and destruction. Plugins are identified by their unique identifiers.
 */
@Component
public class PluginRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(PluginRegistry.class);

    private final ConcurrentMap<String, Plugin> plugins = new ConcurrentHashMap<>();

    /**
     * Registers a plugin in the registry.
     *
     * @param plugin the plugin to register
     * @throws IllegalArgumentException if a plugin with the same ID is already registered
     */
    public void register(final Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin must not be null");
        final var previous = plugins.putIfAbsent(plugin.getId(), plugin);
        if (previous != null) {
            throw new IllegalArgumentException(
                    "Plugin already registered: " + plugin.getId());
        }
        LOG.info("Registered plugin: {} v{}", plugin.getName(), plugin.getVersion());
    }

    /**
     * Returns the plugin with the given identifier, if registered.
     *
     * @param id the plugin identifier
     * @return an {@link Optional} containing the plugin, or empty if not found
     */
    public Optional<Plugin> findById(final String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(plugins.get(id));
    }

    /**
     * Returns all registered plugins as an unmodifiable collection.
     *
     * @return the registered plugins
     */
    public Collection<Plugin> getAll() {
        return Collections.unmodifiableCollection(plugins.values());
    }

    /**
     * Checks whether a plugin with the given identifier is registered.
     *
     * @param id the plugin identifier
     * @return {@code true} if the plugin is registered
     */
    public boolean isRegistered(final String id) {
        Objects.requireNonNull(id, "id must not be null");
        return plugins.containsKey(id);
    }

    /**
     * Removes all registered plugins. Primarily for testing.
     */
    public void clear() {
        plugins.clear();
    }
}
