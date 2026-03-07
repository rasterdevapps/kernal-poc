package com.erp.kernel.businesslogic.engine;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link RuleContext}.
 */
class RuleContextTest {

    @Test
    void shouldBuildContext_withAttributes() {
        final var context = RuleContext.builder("Domain", "CREATE")
                .attribute("name", "TEST")
                .attribute("value", 42)
                .build();

        assertThat(context.getEntityType()).isEqualTo("Domain");
        assertThat(context.getOperation()).isEqualTo("CREATE");
        assertThat(context.getAttributes()).hasSize(2);
        assertThat(context.getAttribute("name")).contains("TEST");
        assertThat(context.getAttribute("value")).contains(42);
    }

    @Test
    void shouldReturnEmpty_whenAttributeNotFound() {
        final var context = RuleContext.builder("Domain", "CREATE").build();

        assertThat(context.getAttribute("missing")).isEmpty();
    }

    @Test
    void shouldReturnUnmodifiableAttributes() {
        final var context = RuleContext.builder("Domain", "CREATE")
                .attribute("key", "value")
                .build();

        assertThatThrownBy(() -> context.getAttributes().put("new", "val"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenEntityTypeIsNull() {
        assertThatThrownBy(() -> RuleContext.builder(null, "CREATE"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenOperationIsNull() {
        assertThatThrownBy(() -> RuleContext.builder("Domain", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenAttributeNameIsNull() {
        final var builder = RuleContext.builder("Domain", "CREATE");
        assertThatThrownBy(() -> builder.attribute(null, "value"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenGetAttributeNameIsNull() {
        final var context = RuleContext.builder("Domain", "CREATE").build();
        assertThatThrownBy(() -> context.getAttribute(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBuildContext_withNoAttributes() {
        final var context = RuleContext.builder("Entity", "DELETE").build();

        assertThat(context.getAttributes()).isEmpty();
    }
}
