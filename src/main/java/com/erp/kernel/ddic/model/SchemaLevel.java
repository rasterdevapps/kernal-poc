package com.erp.kernel.ddic.model;

/**
 * Represents the three schema levels of the ANSI/SPARC architecture.
 *
 * <ul>
 *   <li>{@link #EXTERNAL} — The user/application view of data</li>
 *   <li>{@link #CONCEPTUAL} — The logical structure of the entire database</li>
 *   <li>{@link #INTERNAL} — The physical storage representation</li>
 * </ul>
 */
public enum SchemaLevel {

    /** The user or application view of data. */
    EXTERNAL,

    /** The logical structure of the entire database. */
    CONCEPTUAL,

    /** The physical storage representation. */
    INTERNAL
}
