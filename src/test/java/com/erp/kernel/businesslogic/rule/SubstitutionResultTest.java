package com.erp.kernel.businesslogic.rule;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SubstitutionResult}.
 */
class SubstitutionResultTest {

    @Test
    void shouldCreateNoneResult() {
        final var result = SubstitutionResult.none();

        assertThat(result.applied()).isFalse();
        assertThat(result.substitutions()).isEmpty();
    }

    @Test
    void shouldCreateResultWithSubstitutions() {
        final var subs = Map.<String, Object>of("field1", "value1", "field2", 42);
        final var result = SubstitutionResult.of(subs);

        assertThat(result.applied()).isTrue();
        assertThat(result.substitutions()).hasSize(2);
        assertThat(result.substitutions()).containsEntry("field1", "value1");
    }

    @Test
    void shouldCreateNoneResult_whenSubstitutionsMapIsEmpty() {
        final var result = SubstitutionResult.of(Map.of());

        assertThat(result.applied()).isFalse();
        assertThat(result.substitutions()).isEmpty();
    }

    @Test
    void shouldReturnImmutableSubstitutions() {
        final var mutableMap = new HashMap<String, Object>();
        mutableMap.put("field", "value");
        final var result = SubstitutionResult.of(mutableMap);
        mutableMap.put("extra", "value2");

        assertThat(result.substitutions()).hasSize(1);
    }
}
