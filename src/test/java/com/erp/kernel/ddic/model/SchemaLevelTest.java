package com.erp.kernel.ddic.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SchemaLevel}.
 */
class SchemaLevelTest {

    @Test
    void shouldHaveThreeValues() {
        assertThat(SchemaLevel.values()).hasSize(3);
    }

    @Test
    void shouldContainExternal() {
        assertThat(SchemaLevel.valueOf("EXTERNAL")).isEqualTo(SchemaLevel.EXTERNAL);
    }

    @Test
    void shouldContainConceptual() {
        assertThat(SchemaLevel.valueOf("CONCEPTUAL")).isEqualTo(SchemaLevel.CONCEPTUAL);
    }

    @Test
    void shouldContainInternal() {
        assertThat(SchemaLevel.valueOf("INTERNAL")).isEqualTo(SchemaLevel.INTERNAL);
    }
}
