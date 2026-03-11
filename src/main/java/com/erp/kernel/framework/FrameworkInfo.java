package com.erp.kernel.framework;

import java.util.List;
import java.util.Objects;

/**
 * Provides metadata about the ERP Kernel framework.
 *
 * <p>Contains the framework name, version, and a list of core capabilities
 * that are packaged as part of the reusable ERP development platform.
 *
 * @param name         the framework name
 * @param version      the framework version
 * @param capabilities the list of core framework capabilities
 */
public record FrameworkInfo(
        String name,
        String version,
        List<String> capabilities
) {

    /**
     * Creates a validated framework info instance.
     *
     * @param name         the framework name (required)
     * @param version      the framework version (required)
     * @param capabilities the capabilities (required, may be empty)
     */
    public FrameworkInfo {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(version, "version must not be null");
        Objects.requireNonNull(capabilities, "capabilities must not be null");
        capabilities = List.copyOf(capabilities);
    }
}
