package com.erp.kernel.numberrange.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NumberRangeInterval}.
 */
class NumberRangeIntervalTest {

    @Test
    void shouldSetAndGetAllFields() {
        final var range = new NumberRange();
        range.setRangeObject("DOC_NR");

        final var interval = new NumberRangeInterval();
        interval.setNumberRange(range);
        interval.setSubObject("01");
        interval.setFromNumber("0000000001");
        interval.setToNumber("9999999999");
        interval.setCurrentNumber("0000000050");

        assertThat(interval.getNumberRange()).isSameAs(range);
        assertThat(interval.getSubObject()).isEqualTo("01");
        assertThat(interval.getFromNumber()).isEqualTo("0000000001");
        assertThat(interval.getToNumber()).isEqualTo("9999999999");
        assertThat(interval.getCurrentNumber()).isEqualTo("0000000050");
    }
}
