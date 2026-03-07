package com.erp.kernel.numberrange.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.numberrange.dto.CreateIntervalRequest;
import com.erp.kernel.numberrange.dto.CreateNumberRangeRequest;
import com.erp.kernel.numberrange.entity.NumberRange;
import com.erp.kernel.numberrange.entity.NumberRangeInterval;
import com.erp.kernel.numberrange.repository.NumberRangeIntervalRepository;
import com.erp.kernel.numberrange.repository.NumberRangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link NumberRangeService}.
 */
@ExtendWith(MockitoExtension.class)
class NumberRangeServiceTest {

    @Mock
    private NumberRangeRepository numberRangeRepository;

    @Mock
    private NumberRangeIntervalRepository intervalRepository;

    @InjectMocks
    private NumberRangeService numberRangeService;

    // --- create ---
    @Test
    void shouldCreateNumberRange_whenNameIsUnique() {
        final var request = new CreateNumberRangeRequest("ORDER_NR", "Orders", true, 100);
        when(numberRangeRepository.existsByRangeObject("ORDER_NR")).thenReturn(false);
        when(numberRangeRepository.save(any(NumberRange.class))).thenAnswer(invocation -> {
            final var saved = invocation.<NumberRange>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = numberRangeService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.rangeObject()).isEqualTo("ORDER_NR");
        verify(numberRangeRepository).save(any(NumberRange.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenRangeExists() {
        final var request = new CreateNumberRangeRequest("ORDER_NR", "Orders", true, 100);
        when(numberRangeRepository.existsByRangeObject("ORDER_NR")).thenReturn(true);

        assertThatThrownBy(() -> numberRangeService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("ORDER_NR");
        verify(numberRangeRepository, never()).save(any());
    }

    // --- findById ---
    @Test
    void shouldReturnNumberRange_whenFoundById() {
        final var entity = createRange(1L, "ORDER_NR");
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = numberRangeService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.rangeObject()).isEqualTo("ORDER_NR");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenRangeNotFoundById() {
        when(numberRangeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberRangeService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- findAll ---
    @Test
    void shouldReturnAllNumberRanges() {
        when(numberRangeRepository.findAll()).thenReturn(List.of(
                createRange(1L, "ORDER_NR"),
                createRange(2L, "DOC_NR")));

        final var result = numberRangeService.findAll();

        assertThat(result).hasSize(2);
    }

    // --- update ---
    @Test
    void shouldUpdateNumberRange_whenIdExistsAndNameIsUnique() {
        final var entity = createRange(1L, "OLD_NR");
        final var request = new CreateNumberRangeRequest("NEW_NR", "Updated", false, null);
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(numberRangeRepository.findByRangeObject("NEW_NR")).thenReturn(Optional.empty());
        when(numberRangeRepository.save(any(NumberRange.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = numberRangeService.update(1L, request);

        assertThat(result.rangeObject()).isEqualTo("NEW_NR");
    }

    @Test
    void shouldUpdateNumberRange_whenNameUnchanged() {
        final var entity = createRange(1L, "SAME_NR");
        final var request = new CreateNumberRangeRequest("SAME_NR", "Updated", false, null);
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(numberRangeRepository.findByRangeObject("SAME_NR")).thenReturn(Optional.of(entity));
        when(numberRangeRepository.save(any(NumberRange.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = numberRangeService.update(1L, request);

        assertThat(result.rangeObject()).isEqualTo("SAME_NR");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentRange() {
        final var request = new CreateNumberRangeRequest("NR", "desc", false, null);
        when(numberRangeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberRangeService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateNameConflicts() {
        final var entity = createRange(1L, "ORIGINAL");
        final var conflicting = createRange(2L, "TAKEN");
        final var request = new CreateNumberRangeRequest("TAKEN", "desc", false, null);
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(numberRangeRepository.findByRangeObject("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> numberRangeService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("TAKEN");
    }

    // --- delete ---
    @Test
    void shouldDeleteNumberRange_whenIdExists() {
        when(numberRangeRepository.existsById(1L)).thenReturn(true);

        numberRangeService.delete(1L);

        verify(numberRangeRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentRange() {
        when(numberRangeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> numberRangeService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // --- createInterval ---
    @Test
    void shouldCreateInterval_whenValid() {
        final var range = createRange(1L, "ORDER_NR");
        final var request = new CreateIntervalRequest("01", "1", "999");
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(range));
        when(intervalRepository.findByNumberRangeAndSubObject(range, "01"))
                .thenReturn(Optional.empty());
        when(intervalRepository.save(any(NumberRangeInterval.class))).thenAnswer(invocation -> {
            final var saved = invocation.<NumberRangeInterval>getArgument(0);
            saved.setId(10L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = numberRangeService.createInterval(1L, request);

        assertThat(result.subObject()).isEqualTo("01");
        assertThat(result.fromNumber()).isEqualTo("1");
        assertThat(result.currentNumber()).isEqualTo("1");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenRangeNotFoundForInterval() {
        final var request = new CreateIntervalRequest("01", "1", "999");
        when(numberRangeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberRangeService.createInterval(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenIntervalExists() {
        final var range = createRange(1L, "ORDER_NR");
        final var existing = new NumberRangeInterval();
        final var request = new CreateIntervalRequest("01", "1", "999");
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(range));
        when(intervalRepository.findByNumberRangeAndSubObject(range, "01"))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> numberRangeService.createInterval(1L, request))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    void shouldThrowValidationException_whenFromExceedsTo() {
        final var range = createRange(1L, "ORDER_NR");
        final var request = new CreateIntervalRequest("01", "999", "1");
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(range));
        when(intervalRepository.findByNumberRangeAndSubObject(range, "01"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberRangeService.createInterval(1L, request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("From number");
    }

    // --- findIntervals ---
    @Test
    void shouldReturnIntervals_whenRangeExists() {
        final var range = createRange(1L, "ORDER_NR");
        final var interval = createInterval(range, "01");
        when(numberRangeRepository.findById(1L)).thenReturn(Optional.of(range));
        when(intervalRepository.findByNumberRange(range)).thenReturn(List.of(interval));

        final var result = numberRangeService.findIntervals(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenRangeNotFoundForIntervals() {
        when(numberRangeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberRangeService.findIntervals(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // --- getNextNumber ---
    @Test
    void shouldAssignNextNumber_whenIntervalAvailable() {
        final var range = createRange(1L, "ORDER_NR");
        final var interval = createInterval(range, "01");
        interval.setCurrentNumber("50");
        interval.setToNumber("999");
        when(numberRangeRepository.findByRangeObject("ORDER_NR")).thenReturn(Optional.of(range));
        when(intervalRepository.findByNumberRangeAndSubObject(range, "01"))
                .thenReturn(Optional.of(interval));
        when(intervalRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = numberRangeService.getNextNumber("ORDER_NR", "01");

        assertThat(result.assignedNumber()).isEqualTo("51");
        assertThat(interval.getCurrentNumber()).isEqualTo("51");
    }

    @Test
    void shouldThrowValidationException_whenIntervalExhausted() {
        final var range = createRange(1L, "ORDER_NR");
        final var interval = createInterval(range, "01");
        interval.setCurrentNumber("999");
        interval.setToNumber("999");
        when(numberRangeRepository.findByRangeObject("ORDER_NR")).thenReturn(Optional.of(range));
        when(intervalRepository.findByNumberRangeAndSubObject(range, "01"))
                .thenReturn(Optional.of(interval));

        assertThatThrownBy(() -> numberRangeService.getNextNumber("ORDER_NR", "01"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("exhausted");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenRangeNotFoundForNextNumber() {
        when(numberRangeRepository.findByRangeObject("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberRangeService.getNextNumber("UNKNOWN", "01"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenIntervalNotFoundForNextNumber() {
        final var range = createRange(1L, "ORDER_NR");
        when(numberRangeRepository.findByRangeObject("ORDER_NR")).thenReturn(Optional.of(range));
        when(intervalRepository.findByNumberRangeAndSubObject(range, "99"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberRangeService.getNextNumber("ORDER_NR", "99"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private NumberRange createRange(final Long id, final String name) {
        final var entity = new NumberRange();
        entity.setId(id);
        entity.setRangeObject(name);
        entity.setDescription("Test");
        entity.setBuffered(true);
        entity.setBufferSize(100);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    private NumberRangeInterval createInterval(final NumberRange range, final String subObject) {
        final var interval = new NumberRangeInterval();
        interval.setId(10L);
        interval.setNumberRange(range);
        interval.setSubObject(subObject);
        interval.setFromNumber("1");
        interval.setToNumber("999");
        interval.setCurrentNumber("1");
        interval.setCreatedAt(Instant.now());
        interval.setUpdatedAt(Instant.now());
        return interval;
    }
}
