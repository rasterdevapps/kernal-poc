package com.erp.kernel.vertical;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Registry for managing vertical-specific modules.
 *
 * <p>Provides thread-safe registration, lookup, and filtering of
 * {@link VerticalModule} instances by identifier and vertical type.
 */
@Component
public class VerticalRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(VerticalRegistry.class);

    private final ConcurrentMap<String, VerticalModule> modules = new ConcurrentHashMap<>();

    /**
     * Registers a vertical module in the registry.
     *
     * @param module the vertical module to register
     * @throws IllegalArgumentException if a module with the same ID is already registered
     */
    public void register(final VerticalModule module) {
        Objects.requireNonNull(module, "module must not be null");
        final var previous = modules.putIfAbsent(module.getId(), module);
        if (previous != null) {
            throw new IllegalArgumentException(
                    "Vertical module already registered: " + module.getId());
        }
        LOG.info("Registered vertical module: {} ({})",
                module.getName(), module.getVerticalType());
    }

    /**
     * Returns the module with the given identifier, if registered.
     *
     * @param id the module identifier
     * @return an {@link Optional} containing the module, or empty if not found
     */
    public Optional<VerticalModule> findById(final String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(modules.get(id));
    }

    /**
     * Returns all modules belonging to the specified vertical type.
     *
     * @param verticalType the vertical type to filter by
     * @return the matching modules
     */
    public List<VerticalModule> findByVerticalType(final VerticalType verticalType) {
        Objects.requireNonNull(verticalType, "verticalType must not be null");
        return modules.values().stream()
                .filter(m -> m.getVerticalType() == verticalType)
                .toList();
    }

    /**
     * Returns all registered vertical modules as an unmodifiable collection.
     *
     * @return the registered modules
     */
    public Collection<VerticalModule> getAll() {
        return Collections.unmodifiableCollection(modules.values());
    }

    /**
     * Checks whether a module with the given identifier is registered.
     *
     * @param id the module identifier
     * @return {@code true} if the module is registered
     */
    public boolean isRegistered(final String id) {
        Objects.requireNonNull(id, "id must not be null");
        return modules.containsKey(id);
    }

    /**
     * Removes all registered modules. Primarily for testing.
     */
    public void clear() {
        modules.clear();
    }
}
