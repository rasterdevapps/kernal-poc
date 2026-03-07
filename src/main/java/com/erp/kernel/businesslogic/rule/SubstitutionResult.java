package com.erp.kernel.businesslogic.rule;

import java.util.Map;

/**
 * Result of a substitution rule evaluation.
 *
 * <p>Contains a map of field names to their substituted values.
 * Only fields that were modified are included in the result.
 *
 * @param applied       whether any substitutions were applied
 * @param substitutions a map of field name to substituted value
 */
public record SubstitutionResult(boolean applied, Map<String, Object> substitutions) {

    /**
     * Creates a result indicating no substitutions were applied.
     *
     * @return an empty substitution result
     */
    public static SubstitutionResult none() {
        return new SubstitutionResult(false, Map.of());
    }

    /**
     * Creates a result with the given substitutions.
     *
     * @param substitutions the field-to-value substitution map
     * @return a substitution result with applied changes
     */
    public static SubstitutionResult of(final Map<String, Object> substitutions) {
        return new SubstitutionResult(!substitutions.isEmpty(), Map.copyOf(substitutions));
    }
}
