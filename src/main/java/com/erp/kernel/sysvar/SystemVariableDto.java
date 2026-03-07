package com.erp.kernel.sysvar;

/**
 * Response DTO for a single system variable.
 *
 * @param variableName the system variable name (e.g., SY-DATUM)
 * @param value        the current value
 * @param description  the variable description
 * @param dataType     the ABAP data type
 */
public record SystemVariableDto(
        String variableName,
        String value,
        String description,
        String dataType
) {
}
