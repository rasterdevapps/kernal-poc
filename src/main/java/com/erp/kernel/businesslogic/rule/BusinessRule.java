package com.erp.kernel.businesslogic.rule;

/**
 * Base interface for all business rules.
 *
 * <p>A business rule evaluates a context and may produce a result such as
 * a validation outcome or a data substitution. Rules are identified by a
 * unique name and can be activated or deactivated.
 *
 * @param <T> the type of the rule context
 * @param <R> the type of the rule result
 */
public interface BusinessRule<T, R> {

    /**
     * Returns the unique name of this rule.
     *
     * @return the rule name
     */
    String getName();

    /**
     * Returns a human-readable description of this rule.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Indicates whether this rule is currently active.
     *
     * @return {@code true} if the rule is active
     */
    boolean isActive();

    /**
     * Evaluates this rule against the given context.
     *
     * @param context the evaluation context
     * @return the rule result
     */
    R evaluate(T context);
}
