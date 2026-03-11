package com.erp.kernel.vertical;

import java.util.List;
import java.util.Objects;

/**
 * Immutable descriptor for a vertical module.
 *
 * <p>Captures the module's identity, vertical type, version, description,
 * and required framework capabilities for deployment validation.
 *
 * @param id                   the unique module identifier
 * @param name                 the module display name
 * @param verticalType         the industry vertical
 * @param version              the module version
 * @param description          a brief description
 * @param requiredCapabilities the framework capabilities this module depends on
 */
public record VerticalDescriptor(
        String id,
        String name,
        VerticalType verticalType,
        String version,
        String description,
        List<String> requiredCapabilities
) implements VerticalModule {

    /**
     * Creates a validated vertical descriptor.
     *
     * @param id                   the module identifier (required)
     * @param name                 the module name (required)
     * @param verticalType         the vertical type (required)
     * @param version              the module version (required)
     * @param description          the description (required)
     * @param requiredCapabilities the required capabilities (required, may be empty)
     */
    public VerticalDescriptor {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(verticalType, "verticalType must not be null");
        Objects.requireNonNull(version, "version must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(requiredCapabilities, "requiredCapabilities must not be null");
        requiredCapabilities = List.copyOf(requiredCapabilities);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public VerticalType getVerticalType() {
        return verticalType;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public List<String> getRequiredCapabilities() {
        return requiredCapabilities;
    }
}
