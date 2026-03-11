package com.erp.kernel.vertical;

/**
 * Enumerates the industry verticals supported by the ERP Kernel framework.
 *
 * <p>Each vertical represents a distinct industry domain that can be
 * deployed on the common ERP platform.
 */
public enum VerticalType {

    /** Healthcare and veterinary industry (One Health). */
    HEALTHCARE("Healthcare & One Health"),

    /** Manufacturing and production industry. */
    MANUFACTURING("Manufacturing & Production"),

    /** Retail and distribution industry. */
    RETAIL("Retail & Distribution"),

    /** Financial services industry. */
    FINANCE("Financial Services"),

    /** Education and academic institutions. */
    EDUCATION("Education & Academic");

    private final String displayName;

    VerticalType(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name of this vertical.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
