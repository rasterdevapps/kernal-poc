package com.erp.kernel.framework;

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
 * Registry for managing ERP framework modules.
 *
 * <p>Provides thread-safe registration, lookup, and lifecycle management
 * of {@link ErpModule} instances. Modules are identified by unique names.
 */
@Component
public class ErpModuleRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(ErpModuleRegistry.class);

    private final ConcurrentMap<String, ErpModule> modules = new ConcurrentHashMap<>();

    /**
     * Registers an ERP module in the registry.
     *
     * @param module the module to register
     * @throws IllegalArgumentException if a module with the same name is already registered
     */
    public void register(final ErpModule module) {
        Objects.requireNonNull(module, "module must not be null");
        final var previous = modules.putIfAbsent(module.getName(), module);
        if (previous != null) {
            throw new IllegalArgumentException(
                    "Module already registered: " + module.getName());
        }
        LOG.info("Registered ERP module: {} v{}", module.getName(), module.getVersion());
    }

    /**
     * Returns the module with the given name, if registered.
     *
     * @param name the module name
     * @return an {@link Optional} containing the module, or empty if not found
     */
    public Optional<ErpModule> findByName(final String name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(modules.get(name));
    }

    /**
     * Returns all registered modules as an unmodifiable collection.
     *
     * @return the registered modules
     */
    public Collection<ErpModule> getAll() {
        return Collections.unmodifiableCollection(modules.values());
    }

    /**
     * Checks whether a module with the given name is registered.
     *
     * @param name the module name
     * @return {@code true} if the module is registered
     */
    public boolean isRegistered(final String name) {
        Objects.requireNonNull(name, "name must not be null");
        return modules.containsKey(name);
    }

    /**
     * Removes all registered modules. Primarily for testing.
     */
    public void clear() {
        modules.clear();
    }
}
