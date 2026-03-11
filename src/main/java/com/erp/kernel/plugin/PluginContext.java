package com.erp.kernel.plugin;

import java.util.Map;
import java.util.Objects;

/**
 * Provides access to kernel services and configuration for plugins.
 *
 * <p>A plugin context is passed to a plugin during initialization, giving it
 * access to the framework name, version, and a set of named properties.
 *
 * @param frameworkName    the name of the ERP framework
 * @param frameworkVersion the framework version
 * @param properties       configuration properties available to the plugin
 */
public record PluginContext(
        String frameworkName,
        String frameworkVersion,
        Map<String, String> properties
) {

    /**
     * Creates a validated plugin context.
     *
     * @param frameworkName    the framework name (required)
     * @param frameworkVersion the framework version (required)
     * @param properties       the plugin properties (required, may be empty)
     */
    public PluginContext {
        Objects.requireNonNull(frameworkName, "frameworkName must not be null");
        Objects.requireNonNull(frameworkVersion, "frameworkVersion must not be null");
        Objects.requireNonNull(properties, "properties must not be null");
        properties = Map.copyOf(properties);
    }
}
