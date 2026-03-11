package com.erp.kernel.plugin;

import java.util.List;

/**
 * Defines the contract for an ERP Kernel plugin.
 *
 * <p>Plugins extend the kernel with vertical-specific functionality
 * (e.g., healthcare, manufacturing, retail). Each plugin has a unique
 * identifier, a lifecycle, and declares its dependencies.
 */
public interface Plugin {

    /**
     * Returns the unique identifier of this plugin.
     *
     * @return the plugin identifier
     */
    String getId();

    /**
     * Returns a human-readable name for this plugin.
     *
     * @return the plugin name
     */
    String getName();

    /**
     * Returns the version of this plugin.
     *
     * @return the plugin version
     */
    String getVersion();

    /**
     * Returns the current lifecycle state of this plugin.
     *
     * @return the plugin state
     */
    PluginState getState();

    /**
     * Returns the identifiers of plugins this plugin depends on.
     *
     * @return the dependency identifiers
     */
    List<String> getDependencies();

    /**
     * Initializes the plugin. Called once after creation.
     *
     * @param context the plugin context providing access to kernel services
     */
    void initialize(PluginContext context);

    /**
     * Starts the plugin, making its functionality available.
     */
    void start();

    /**
     * Stops the plugin, suspending its functionality.
     */
    void stop();

    /**
     * Destroys the plugin, releasing all resources.
     */
    void destroy();
}
