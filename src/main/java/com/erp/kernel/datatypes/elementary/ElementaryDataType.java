package com.erp.kernel.datatypes.elementary;

/**
 * Defines ABAP-inspired elementary data types mapped to Java and PostgreSQL types.
 *
 * <p>Each elementary type specifies:
 * <ul>
 *   <li>A Java class for in-memory representation</li>
 *   <li>A PostgreSQL column type for database storage</li>
 *   <li>Whether the type supports length and decimal constraints</li>
 * </ul>
 */
public enum ElementaryDataType {

    /** Character string (fixed length). */
    CHAR("java.lang.String", "VARCHAR", true, false, "Character string"),

    /** Numeric character string (digits only, preserves leading zeros). */
    NUMC("java.lang.String", "VARCHAR", true, false, "Numeric character string"),

    /** Date in format YYYYMMDD. */
    DATS("java.time.LocalDate", "DATE", false, false, "Date (YYYYMMDD)"),

    /** Time in format HHMMSS. */
    TIMS("java.time.LocalTime", "TIME", false, false, "Time (HHMMSS)"),

    /** 1-byte integer (0 to 255). */
    INT1("java.lang.Short", "SMALLINT", false, false, "1-byte integer"),

    /** 2-byte integer (-32768 to 32767). */
    INT2("java.lang.Short", "SMALLINT", false, false, "2-byte integer"),

    /** 4-byte integer. */
    INT4("java.lang.Integer", "INTEGER", false, false, "4-byte integer"),

    /** 8-byte integer. */
    INT8("java.lang.Long", "BIGINT", false, false, "8-byte integer"),

    /** Packed decimal with fixed precision. */
    DEC("java.math.BigDecimal", "NUMERIC", true, true, "Packed decimal"),

    /** Currency amount with fixed decimal places. */
    CURR("java.math.BigDecimal", "NUMERIC", true, true, "Currency amount"),

    /** Quantity with unit reference. */
    QUAN("java.math.BigDecimal", "NUMERIC", true, true, "Quantity"),

    /** Floating point number. */
    FLTP("java.lang.Double", "DOUBLE PRECISION", false, false, "Floating point"),

    /** Variable-length character string. */
    STRING("java.lang.String", "TEXT", false, false, "Variable-length string"),

    /** Variable-length byte string. */
    RAWSTRING("byte[]", "BYTEA", false, false, "Variable-length byte string"),

    /** Fixed-length byte sequence. */
    RAW("byte[]", "BYTEA", true, false, "Fixed-length byte sequence"),

    /** Client identifier (3 characters). */
    CLNT("java.lang.String", "VARCHAR(3)", false, false, "Client identifier"),

    /** Language key (1 character). */
    LANG("java.lang.String", "VARCHAR(1)", false, false, "Language key"),

    /** Unit of measurement key. */
    UNIT("java.lang.String", "VARCHAR(3)", false, false, "Unit of measurement"),

    /** Currency key (ISO 4217). */
    CUKY("java.lang.String", "VARCHAR(5)", false, false, "Currency key"),

    /** Accounting period (YYYYMM). */
    ACCP("java.lang.String", "VARCHAR(6)", false, false, "Accounting period");

    private final String javaType;
    private final String postgresType;
    private final boolean supportsLength;
    private final boolean supportsDecimals;
    private final String description;

    ElementaryDataType(final String javaType,
                       final String postgresType,
                       final boolean supportsLength,
                       final boolean supportsDecimals,
                       final String description) {
        this.javaType = javaType;
        this.postgresType = postgresType;
        this.supportsLength = supportsLength;
        this.supportsDecimals = supportsDecimals;
        this.description = description;
    }

    /**
     * Returns the fully qualified Java type name for this data type.
     *
     * @return the Java type name
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * Returns the PostgreSQL column type for this data type.
     *
     * @return the PostgreSQL type
     */
    public String getPostgresType() {
        return postgresType;
    }

    /**
     * Indicates whether this type supports a length constraint.
     *
     * @return {@code true} if the type supports length
     */
    public boolean isSupportsLength() {
        return supportsLength;
    }

    /**
     * Indicates whether this type supports decimal places.
     *
     * @return {@code true} if the type supports decimals
     */
    public boolean isSupportsDecimals() {
        return supportsDecimals;
    }

    /**
     * Returns a human-readable description of this data type.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Resolves the PostgreSQL column definition including optional length and decimal precision.
     *
     * @param length        the field length (may be {@code null})
     * @param decimalPlaces the decimal places (may be {@code null})
     * @return the full PostgreSQL column type definition
     */
    public String resolvePostgresType(final Integer length, final Integer decimalPlaces) {
        if (supportsLength && supportsDecimals && length != null) {
            final var decimals = decimalPlaces != null ? decimalPlaces : 0;
            return "%s(%d,%d)".formatted(postgresType, length, decimals);
        }
        if (supportsLength && length != null) {
            return "%s(%d)".formatted(postgresType, length);
        }
        return postgresType;
    }
}
