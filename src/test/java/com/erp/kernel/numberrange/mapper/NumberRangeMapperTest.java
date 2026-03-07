package com.erp.kernel.numberrange.mapper;

import com.erp.kernel.numberrange.dto.CreateIntervalRequest;
import com.erp.kernel.numberrange.dto.CreateNumberRangeRequest;
import com.erp.kernel.numberrange.entity.NumberRange;
import com.erp.kernel.numberrange.entity.NumberRangeInterval;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link NumberRangeMapper}.
 */
class NumberRangeMapperTest {

    @Test
    void shouldConvertEntityToDto() {
        final var entity = createNumberRange(1L, "ORDER_NR");
        final var dto = NumberRangeMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.rangeObject()).isEqualTo("ORDER_NR");
        assertThat(dto.description()).isEqualTo("Test range");
        assertThat(dto.buffered()).isTrue();
        assertThat(dto.bufferSize()).isEqualTo(100);
    }

    @Test
    void shouldConvertRequestToEntity() {
        final var request = new CreateNumberRangeRequest("DOC_NR", "Documents", false, null);
        final var entity = NumberRangeMapper.toEntity(request);

        assertThat(entity.getRangeObject()).isEqualTo("DOC_NR");
        assertThat(entity.getDescription()).isEqualTo("Documents");
        assertThat(entity.isBuffered()).isFalse();
        assertThat(entity.getBufferSize()).isNull();
    }

    @Test
    void shouldUpdateEntityFromRequest() {
        final var entity = createNumberRange(1L, "OLD_NR");
        final var request = new CreateNumberRangeRequest("NEW_NR", "Updated", false, null);

        NumberRangeMapper.updateEntity(entity, request);

        assertThat(entity.getRangeObject()).isEqualTo("NEW_NR");
        assertThat(entity.getDescription()).isEqualTo("Updated");
        assertThat(entity.isBuffered()).isFalse();
    }

    @Test
    void shouldConvertIntervalEntityToDto() {
        final var range = createNumberRange(1L, "ORDER_NR");
        final var interval = new NumberRangeInterval();
        interval.setId(10L);
        interval.setNumberRange(range);
        interval.setSubObject("01");
        interval.setFromNumber("1");
        interval.setToNumber("999");
        interval.setCurrentNumber("50");
        interval.setCreatedAt(Instant.now());
        interval.setUpdatedAt(Instant.now());

        final var dto = NumberRangeMapper.toIntervalDto(interval);

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.numberRangeId()).isEqualTo(1L);
        assertThat(dto.subObject()).isEqualTo("01");
        assertThat(dto.fromNumber()).isEqualTo("1");
        assertThat(dto.toNumber()).isEqualTo("999");
        assertThat(dto.currentNumber()).isEqualTo("50");
    }

    @Test
    void shouldConvertIntervalRequestToEntity() {
        final var range = createNumberRange(1L, "ORDER_NR");
        final var request = new CreateIntervalRequest("01", "1", "999");

        final var entity = NumberRangeMapper.toIntervalEntity(request, range);

        assertThat(entity.getNumberRange()).isSameAs(range);
        assertThat(entity.getSubObject()).isEqualTo("01");
        assertThat(entity.getFromNumber()).isEqualTo("1");
        assertThat(entity.getToNumber()).isEqualTo("999");
        assertThat(entity.getCurrentNumber()).isEqualTo("1");
    }

    @Test
    void shouldThrowNullPointerException_whenToDtoEntityIsNull() {
        assertThatThrownBy(() -> NumberRangeMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityRequestIsNull() {
        assertThatThrownBy(() -> NumberRangeMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateEntityIsNull() {
        final var request = new CreateNumberRangeRequest("NR", "desc", false, null);
        assertThatThrownBy(() -> NumberRangeMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateRequestIsNull() {
        final var entity = createNumberRange(1L, "NR");
        assertThatThrownBy(() -> NumberRangeMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToIntervalDtoEntityIsNull() {
        assertThatThrownBy(() -> NumberRangeMapper.toIntervalDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToIntervalEntityRequestIsNull() {
        final var range = createNumberRange(1L, "NR");
        assertThatThrownBy(() -> NumberRangeMapper.toIntervalEntity(null, range))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToIntervalEntityRangeIsNull() {
        final var request = new CreateIntervalRequest("01", "1", "999");
        assertThatThrownBy(() -> NumberRangeMapper.toIntervalEntity(request, null))
                .isInstanceOf(NullPointerException.class);
    }

    private NumberRange createNumberRange(final Long id, final String name) {
        final var entity = new NumberRange();
        entity.setId(id);
        entity.setRangeObject(name);
        entity.setDescription("Test range");
        entity.setBuffered(true);
        entity.setBufferSize(100);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }
}
