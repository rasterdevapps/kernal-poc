package com.erp.kernel.plugin;

/**
 * Represents the lifecycle states of a plugin.
 *
 * <p>Plugins transition through these states during their lifecycle:
 * {@code CREATED → INITIALIZED → STARTED → STOPPED → DESTROYED}.
 */
public enum PluginState {

    /** Plugin instance has been created but not yet initialized. */
    CREATED,

    /** Plugin has been initialized and is ready to start. */
    INITIALIZED,

    /** Plugin is actively running. */
    STARTED,

    /** Plugin has been stopped but can be restarted. */
    STOPPED,

    /** Plugin has been destroyed and cannot be restarted. */
    DESTROYED
}
