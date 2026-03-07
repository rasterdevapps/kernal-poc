package com.erp.kernel.businesslogic.rule;

import java.util.List;

/**
 * Result of a validation rule evaluation.
 *
 * <p>Contains the overall validity flag and a list of individual messages
 * describing each validation finding.
 *
 * @param valid    whether the validation passed
 * @param messages the list of validation messages (empty if valid)
 */
public record ValidationResult(boolean valid, List<String> messages) {

    /**
     * Creates a successful validation result with no messages.
     *
     * @return a valid result
     */
    public static ValidationResult success() {
        return new ValidationResult(true, List.of());
    }

    /**
     * Creates a failed validation result with the given messages.
     *
     * @param messages the validation failure messages
     * @return an invalid result
     */
    public static ValidationResult failure(final List<String> messages) {
        return new ValidationResult(false, List.copyOf(messages));
    }

    /**
     * Creates a failed validation result with a single message.
     *
     * @param message the validation failure message
     * @return an invalid result
     */
    public static ValidationResult failure(final String message) {
        return new ValidationResult(false, List.of(message));
    }
}
