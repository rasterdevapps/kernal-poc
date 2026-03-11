package com.erp.kernel.framework;

/**
 * Defines the contract for an ERP framework module.
 *
 * <p>Each module represents a reusable capability of the ERP development platform.
 * Modules are identified by a unique name and version, and describe their
 * dependencies on other modules.
 */
public interface ErpModule {

    /**
     * Returns the unique name of this module.
     *
     * @return the module name
     */
    String getName();

    /**
     * Returns the version of this module.
     *
     * @return the module version
     */
    String getVersion();

    /**
     * Returns a human-readable description of this module.
     *
     * @return the module description
     */
    String getDescription();

    /**
     * Returns the names of modules this module depends on.
     *
     * @return the dependency names
     */
    java.util.List<String> getDependencies();

    /**
     * Indicates whether this module is currently enabled.
     *
     * @return {@code true} if the module is enabled
     */
    boolean isEnabled();
}
