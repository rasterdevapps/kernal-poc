package com.erp.kernel.businesslogic.engine;

import com.erp.kernel.businesslogic.rule.BusinessRule;
import com.erp.kernel.businesslogic.rule.SubstitutionResult;
import com.erp.kernel.businesslogic.rule.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link RuleEngine}.
 */
class RuleEngineTest {

    private RuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        ruleEngine = new RuleEngine();
    }

    @Test
    void shouldReturnSuccess_whenNoValidationRulesRegistered() {
        final var context = RuleContext.builder("Domain", "CREATE").build();
        final var result = ruleEngine.validate(context);

        assertThat(result.valid()).isTrue();
    }

    @Test
    void shouldReturnSuccess_whenAllValidationRulesPass() {
        ruleEngine.registerValidationRule(createValidationRule("rule1", true, true));
        final var context = RuleContext.builder("Domain", "CREATE").build();

        final var result = ruleEngine.validate(context);

        assertThat(result.valid()).isTrue();
    }

    @Test
    void shouldReturnFailure_whenValidationRuleFails() {
        ruleEngine.registerValidationRule(createValidationRule("rule1", true, false));
        final var context = RuleContext.builder("Domain", "CREATE").build();

        final var result = ruleEngine.validate(context);

        assertThat(result.valid()).isFalse();
        assertThat(result.messages()).isNotEmpty();
    }

    @Test
    void shouldSkipInactiveValidationRules() {
        ruleEngine.registerValidationRule(createValidationRule("inactive", false, false));
        final var context = RuleContext.builder("Domain", "CREATE").build();

        final var result = ruleEngine.validate(context);

        assertThat(result.valid()).isTrue();
    }

    @Test
    void shouldCombineFailureMessages_fromMultipleRules() {
        ruleEngine.registerValidationRule(createValidationRule("rule1", true, false));
        ruleEngine.registerValidationRule(createValidationRule("rule2", true, false));
        final var context = RuleContext.builder("Domain", "CREATE").build();

        final var result = ruleEngine.validate(context);

        assertThat(result.valid()).isFalse();
        assertThat(result.messages()).hasSize(2);
    }

    @Test
    void shouldReturnNone_whenNoSubstitutionRulesRegistered() {
        final var context = RuleContext.builder("Domain", "CREATE").build();
        final var result = ruleEngine.substitute(context);

        assertThat(result.applied()).isFalse();
    }

    @Test
    void shouldApplySubstitutions_whenRulesRegistered() {
        ruleEngine.registerSubstitutionRule(createSubstitutionRule("sub1", true, true));
        final var context = RuleContext.builder("Domain", "CREATE").build();

        final var result = ruleEngine.substitute(context);

        assertThat(result.applied()).isTrue();
        assertThat(result.substitutions()).containsKey("field");
    }

    @Test
    void shouldSkipInactiveSubstitutionRules() {
        ruleEngine.registerSubstitutionRule(createSubstitutionRule("inactive", false, true));
        final var context = RuleContext.builder("Domain", "CREATE").build();

        final var result = ruleEngine.substitute(context);

        assertThat(result.applied()).isFalse();
    }

    @Test
    void shouldReturnNone_whenSubstitutionRuleReturnsNone() {
        ruleEngine.registerSubstitutionRule(createSubstitutionRule("sub1", true, false));
        final var context = RuleContext.builder("Domain", "CREATE").build();

        final var result = ruleEngine.substitute(context);

        assertThat(result.applied()).isFalse();
    }

    @Test
    void shouldClearAllRules() {
        ruleEngine.registerValidationRule(createValidationRule("v1", true, true));
        ruleEngine.registerSubstitutionRule(createSubstitutionRule("s1", true, true));

        ruleEngine.clearRules();

        assertThat(ruleEngine.getValidationRules()).isEmpty();
        assertThat(ruleEngine.getSubstitutionRules()).isEmpty();
    }

    @Test
    void shouldReturnUnmodifiableValidationRules() {
        assertThatThrownBy(() -> ruleEngine.getValidationRules().add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnUnmodifiableSubstitutionRules() {
        assertThatThrownBy(() -> ruleEngine.getSubstitutionRules().add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenRegisteringNullValidationRule() {
        assertThatThrownBy(() -> ruleEngine.registerValidationRule(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenRegisteringNullSubstitutionRule() {
        assertThatThrownBy(() -> ruleEngine.registerSubstitutionRule(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenValidatingNullContext() {
        assertThatThrownBy(() -> ruleEngine.validate(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenSubstitutingNullContext() {
        assertThatThrownBy(() -> ruleEngine.substitute(null))
                .isInstanceOf(NullPointerException.class);
    }

    private BusinessRule<RuleContext, ValidationResult> createValidationRule(
            final String name, final boolean active, final boolean valid) {
        return new BusinessRule<>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return "Test rule: " + name;
            }

            @Override
            public boolean isActive() {
                return active;
            }

            @Override
            public ValidationResult evaluate(final RuleContext context) {
                if (valid) {
                    return ValidationResult.success();
                }
                return ValidationResult.failure("Failed: " + name);
            }
        };
    }

    private BusinessRule<RuleContext, SubstitutionResult> createSubstitutionRule(
            final String name, final boolean active, final boolean applies) {
        return new BusinessRule<>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return "Test substitution: " + name;
            }

            @Override
            public boolean isActive() {
                return active;
            }

            @Override
            public SubstitutionResult evaluate(final RuleContext context) {
                if (applies) {
                    return SubstitutionResult.of(Map.of("field", "value"));
                }
                return SubstitutionResult.none();
            }
        };
    }
}
