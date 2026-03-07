package com.erp.kernel.sysvar;

import java.util.Map;

/**
 * Provides runtime values for system variables.
 *
 * <p>Implementations supply the current values of all {@link SystemVariable} entries.
 * Values are read-only from the consumer's perspective and are resolved on each access
 * to reflect the current system state.
 */
public interface SystemVariableProvider {

    /**
     * Returns the current value for the specified system variable.
     *
     * @param variable the system variable to resolve
     * @return the current string value of the variable
     */
    String getValue(SystemVariable variable);

    /**
     * Returns all system variable values as an unmodifiable map.
     *
     * @return a map from {@link SystemVariable} to its current string value
     */
    Map<SystemVariable, String> getAllValues();
}
