package com.erp.kernel.businesslogic.engine;

import com.erp.kernel.businesslogic.rule.BusinessRule;
import com.erp.kernel.businesslogic.rule.SubstitutionResult;
import com.erp.kernel.businesslogic.rule.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Executes registered business rules against a {@link RuleContext}.
 *
 * <p>The rule engine maintains separate registries for validation and substitution rules.
 * Rules are evaluated in registration order. Only active rules are executed.
 */
@Component
public class RuleEngine {

    private static final Logger LOG = LoggerFactory.getLogger(RuleEngine.class);

    private final List<BusinessRule<RuleContext, ValidationResult>> validationRules =
            new CopyOnWriteArrayList<>();
    private final List<BusinessRule<RuleContext, SubstitutionResult>> substitutionRules =
            new CopyOnWriteArrayList<>();

    /**
     * Registers a validation rule.
     *
     * @param rule the validation rule to register
     */
    public void registerValidationRule(
            final BusinessRule<RuleContext, ValidationResult> rule) {
        Objects.requireNonNull(rule, "rule must not be null");
        validationRules.add(rule);
        LOG.info("Registered validation rule: {}", rule.getName());
    }

    /**
     * Registers a substitution rule.
     *
     * @param rule the substitution rule to register
     */
    public void registerSubstitutionRule(
            final BusinessRule<RuleContext, SubstitutionResult> rule) {
        Objects.requireNonNull(rule, "rule must not be null");
        substitutionRules.add(rule);
        LOG.info("Registered substitution rule: {}", rule.getName());
    }

    /**
     * Executes all active validation rules against the given context.
     *
     * @param context the rule context
     * @return a combined validation result
     */
    public ValidationResult validate(final RuleContext context) {
        Objects.requireNonNull(context, "context must not be null");
        final var allMessages = new ArrayList<String>();
        for (final var rule : validationRules) {
            if (rule.isActive()) {
                final var result = rule.evaluate(context);
                if (!result.valid()) {
                    allMessages.addAll(result.messages());
                }
            }
        }
        if (allMessages.isEmpty()) {
            return ValidationResult.success();
        }
        return ValidationResult.failure(allMessages);
    }

    /**
     * Executes all active substitution rules against the given context.
     *
     * @param context the rule context
     * @return a combined substitution result
     */
    public SubstitutionResult substitute(final RuleContext context) {
        Objects.requireNonNull(context, "context must not be null");
        final var combined = new HashMap<String, Object>();
        for (final var rule : substitutionRules) {
            if (rule.isActive()) {
                final var result = rule.evaluate(context);
                if (result.applied()) {
                    combined.putAll(result.substitutions());
                }
            }
        }
        if (combined.isEmpty()) {
            return SubstitutionResult.none();
        }
        return SubstitutionResult.of(combined);
    }

    /**
     * Returns the registered validation rules as an unmodifiable list.
     *
     * @return the validation rules
     */
    public List<BusinessRule<RuleContext, ValidationResult>> getValidationRules() {
        return Collections.unmodifiableList(validationRules);
    }

    /**
     * Returns the registered substitution rules as an unmodifiable list.
     *
     * @return the substitution rules
     */
    public List<BusinessRule<RuleContext, SubstitutionResult>> getSubstitutionRules() {
        return Collections.unmodifiableList(substitutionRules);
    }

    /**
     * Removes all registered rules. Primarily for testing.
     */
    public void clearRules() {
        validationRules.clear();
        substitutionRules.clear();
    }
}
