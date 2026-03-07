package com.erp.kernel.numberrange.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NumberRange}.
 */
class NumberRangeTest {

    @Test
    void shouldSetAndGetAllFields() {
        final var entity = new NumberRange();
        entity.setRangeObject("ORDER_NR");
        entity.setDescription("Order numbers");
        entity.setBuffered(true);
        entity.setBufferSize(100);

        assertThat(entity.getRangeObject()).isEqualTo("ORDER_NR");
        assertThat(entity.getDescription()).isEqualTo("Order numbers");
        assertThat(entity.isBuffered()).isTrue();
        assertThat(entity.getBufferSize()).isEqualTo(100);
    }

    @Test
    void shouldHandleNonBufferedRange() {
        final var entity = new NumberRange();
        entity.setBuffered(false);
        entity.setBufferSize(null);

        assertThat(entity.isBuffered()).isFalse();
        assertThat(entity.getBufferSize()).isNull();
    }
}
