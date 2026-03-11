package com.erp.kernel.vertical;

import java.util.List;

/**
 * Defines the contract for a vertical-specific module.
 *
 * <p>Vertical modules encapsulate industry-specific business logic,
 * data models, and workflows that run on the common ERP Kernel framework.
 */
public interface VerticalModule {

    /**
     * Returns the unique identifier of this vertical module.
     *
     * @return the module identifier
     */
    String getId();

    /**
     * Returns the display name of this vertical module.
     *
     * @return the module name
     */
    String getName();

    /**
     * Returns the industry vertical this module belongs to.
     *
     * @return the vertical type
     */
    VerticalType getVerticalType();

    /**
     * Returns the version of this vertical module.
     *
     * @return the module version
     */
    String getVersion();

    /**
     * Returns the framework capabilities required by this module.
     *
     * @return the list of required capabilities
     */
    List<String> getRequiredCapabilities();
}
