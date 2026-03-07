package com.erp.kernel.sysvar;

/**
 * Enumerates the globally accessible, read-only system variables.
 *
 * <p>System variables are modelled after SAP's SY-* fields and provide
 * runtime context information such as the current date, time, user,
 * language, and client.
 */
public enum SystemVariable {

    /** Current system date (YYYYMMDD). */
    SY_DATUM("SY-DATUM", "Current date", "DATS"),

    /** Current system time (HHMMSS). */
    SY_UZEIT("SY-UZEIT", "Current time", "TIMS"),

    /** Current user name. */
    SY_UNAME("SY-UNAME", "Current user name", "CHAR"),

    /** Current logon language key. */
    SY_LANGU("SY-LANGU", "Logon language", "LANG"),

    /** Current client identifier. */
    SY_MANDT("SY-MANDT", "Client number", "CLNT"),

    /** Current host name. */
    SY_HOST("SY-HOST", "Application server host name", "CHAR"),

    /** System identifier. */
    SY_SYSID("SY-SYSID", "System identifier", "CHAR"),

    /** Current page number (for reporting). */
    SY_PAGNO("SY-PAGNO", "Current page number", "INT4"),

    /** Return code of the last operation. */
    SY_SUBRC("SY-SUBRC", "Return code", "INT4"),

    /** Index of the current loop iteration. */
    SY_INDEX("SY-INDEX", "Loop index", "INT4"),

    /** Number of rows affected by the last database operation. */
    SY_DBCNT("SY-DBCNT", "Database row count", "INT4");

    private final String variableName;
    private final String description;
    private final String dataType;

    SystemVariable(final String variableName,
                   final String description,
                   final String dataType) {
        this.variableName = variableName;
        this.description = description;
        this.dataType = dataType;
    }

    /**
     * Returns the system variable name (e.g., SY-DATUM).
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Returns a human-readable description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the ABAP data type of this variable.
     *
     * @return the data type
     */
    public String getDataType() {
        return dataType;
    }
}
