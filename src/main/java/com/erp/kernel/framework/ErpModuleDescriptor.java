package com.erp.kernel.framework;

import java.util.List;
import java.util.Objects;

/**
 * Immutable descriptor for an ERP framework module.
 *
 * <p>Provides metadata about a module including its name, version,
 * description, dependencies, and enabled state.
 *
 * @param name         the unique module name
 * @param version      the module version
 * @param description  a human-readable description
 * @param dependencies the names of required dependency modules
 * @param enabled      whether the module is currently enabled
 */
public record ErpModuleDescriptor(
        String name,
        String version,
        String description,
        List<String> dependencies,
        boolean enabled
) implements ErpModule {

    /**
     * Creates a validated module descriptor.
     *
     * @param name         the module name (required)
     * @param version      the module version (required)
     * @param description  the description (required)
     * @param dependencies the dependency list (required, may be empty)
     * @param enabled      the enabled flag
     */
    public ErpModuleDescriptor {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(version, "version must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(dependencies, "dependencies must not be null");
        dependencies = List.copyOf(dependencies);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Creates a module descriptor with no dependencies.
     *
     * @param name        the module name
     * @param version     the module version
     * @param description the description
     * @return a new enabled module descriptor with no dependencies
     */
    public static ErpModuleDescriptor of(final String name,
                                         final String version,
                                         final String description) {
        return new ErpModuleDescriptor(name, version, description, List.of(), true);
    }
}
