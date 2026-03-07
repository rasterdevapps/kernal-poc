package com.erp.kernel.numberrange.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for number range DTOs.
 */
class NumberRangeDtoTest {

    @Test
    void shouldCreateNumberRangeDto() {
        final var now = Instant.now();
        final var dto = new NumberRangeDto(1L, "ORDER_NR", "Orders", true, 100, now, now);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.rangeObject()).isEqualTo("ORDER_NR");
        assertThat(dto.description()).isEqualTo("Orders");
        assertThat(dto.buffered()).isTrue();
        assertThat(dto.bufferSize()).isEqualTo(100);
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }

    @Test
    void shouldCreateNumberRangeIntervalDto() {
        final var now = Instant.now();
        final var dto = new NumberRangeIntervalDto(1L, 2L, "01", "1", "999", "50", now, now);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.numberRangeId()).isEqualTo(2L);
        assertThat(dto.subObject()).isEqualTo("01");
        assertThat(dto.fromNumber()).isEqualTo("1");
        assertThat(dto.toNumber()).isEqualTo("999");
        assertThat(dto.currentNumber()).isEqualTo("50");
    }

    @Test
    void shouldCreateNextNumberResponse() {
        final var response = new NextNumberResponse("ORDER_NR", "01", "51");

        assertThat(response.rangeObject()).isEqualTo("ORDER_NR");
        assertThat(response.subObject()).isEqualTo("01");
        assertThat(response.assignedNumber()).isEqualTo("51");
    }

    @Test
    void shouldCreateCreateNumberRangeRequest() {
        final var request = new CreateNumberRangeRequest("ORDER_NR", "Orders", true, 100);

        assertThat(request.rangeObject()).isEqualTo("ORDER_NR");
        assertThat(request.description()).isEqualTo("Orders");
        assertThat(request.buffered()).isTrue();
        assertThat(request.bufferSize()).isEqualTo(100);
    }

    @Test
    void shouldCreateCreateIntervalRequest() {
        final var request = new CreateIntervalRequest("01", "1", "999");

        assertThat(request.subObject()).isEqualTo("01");
        assertThat(request.fromNumber()).isEqualTo("1");
        assertThat(request.toNumber()).isEqualTo("999");
    }
}
