package com.erp.kernel.numberrange.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.numberrange.dto.CreateIntervalRequest;
import com.erp.kernel.numberrange.dto.CreateNumberRangeRequest;
import com.erp.kernel.numberrange.dto.NextNumberResponse;
import com.erp.kernel.numberrange.dto.NumberRangeDto;
import com.erp.kernel.numberrange.dto.NumberRangeIntervalDto;
import com.erp.kernel.numberrange.mapper.NumberRangeMapper;
import com.erp.kernel.numberrange.repository.NumberRangeIntervalRepository;
import com.erp.kernel.numberrange.repository.NumberRangeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for number range management.
 *
 * <p>Supports creating number range objects with intervals, and assigning
 * the next sequential number from an interval. Both buffered and non-buffered
 * modes are supported, similar to SAP's SNRO transaction.
 */
@Service
@Transactional(readOnly = true)
public class NumberRangeService {

    private final NumberRangeRepository numberRangeRepository;
    private final NumberRangeIntervalRepository intervalRepository;

    /**
     * Creates a new number range service.
     *
     * @param numberRangeRepository the number range repository
     * @param intervalRepository    the interval repository
     */
    public NumberRangeService(final NumberRangeRepository numberRangeRepository,
                              final NumberRangeIntervalRepository intervalRepository) {
        this.numberRangeRepository = Objects.requireNonNull(
                numberRangeRepository, "numberRangeRepository must not be null");
        this.intervalRepository = Objects.requireNonNull(
                intervalRepository, "intervalRepository must not be null");
    }

    /**
     * Creates a new number range.
     *
     * @param request the creation request
     * @return the created number range DTO
     * @throws DuplicateEntityException if a range with the same name exists
     */
    @Transactional
    public NumberRangeDto create(final CreateNumberRangeRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (numberRangeRepository.existsByRangeObject(request.rangeObject())) {
            throw new DuplicateEntityException(
                    "Number range '%s' already exists".formatted(request.rangeObject()));
        }
        final var entity = NumberRangeMapper.toEntity(request);
        final var saved = numberRangeRepository.save(entity);
        return NumberRangeMapper.toDto(saved);
    }

    /**
     * Finds a number range by its ID.
     *
     * @param id the number range ID
     * @return the number range DTO
     * @throws EntityNotFoundException if the range is not found
     */
    public NumberRangeDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = numberRangeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Number range with id %d not found".formatted(id)));
        return NumberRangeMapper.toDto(entity);
    }

    /**
     * Returns all number ranges.
     *
     * @return the list of number range DTOs
     */
    public List<NumberRangeDto> findAll() {
        return numberRangeRepository.findAll().stream()
                .map(NumberRangeMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing number range.
     *
     * @param id      the number range ID
     * @param request the update request
     * @return the updated number range DTO
     * @throws EntityNotFoundException  if the range is not found
     * @throws DuplicateEntityException if the new name conflicts with another range
     */
    @Transactional
    public NumberRangeDto update(final Long id, final CreateNumberRangeRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = numberRangeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Number range with id %d not found".formatted(id)));
        numberRangeRepository.findByRangeObject(request.rangeObject())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Number range '%s' already exists".formatted(request.rangeObject()));
                });
        NumberRangeMapper.updateEntity(entity, request);
        final var saved = numberRangeRepository.save(entity);
        return NumberRangeMapper.toDto(saved);
    }

    /**
     * Deletes a number range by its ID.
     *
     * @param id the number range ID
     * @throws EntityNotFoundException if the range is not found
     */
    @Transactional
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!numberRangeRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Number range with id %d not found".formatted(id));
        }
        numberRangeRepository.deleteById(id);
    }

    /**
     * Creates a new interval within a number range.
     *
     * @param rangeId the parent number range ID
     * @param request the interval creation request
     * @return the created interval DTO
     * @throws EntityNotFoundException  if the range is not found
     * @throws DuplicateEntityException if the sub-object already exists
     * @throws ValidationException      if the from number exceeds the to number
     */
    @Transactional
    public NumberRangeIntervalDto createInterval(final Long rangeId,
                                                  final CreateIntervalRequest request) {
        Objects.requireNonNull(rangeId, "rangeId must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var numberRange = numberRangeRepository.findById(rangeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Number range with id %d not found".formatted(rangeId)));
        intervalRepository.findByNumberRangeAndSubObject(numberRange, request.subObject())
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Interval '%s' already exists for range '%s'"
                                    .formatted(request.subObject(), numberRange.getRangeObject()));
                });
        if (Long.parseLong(request.fromNumber()) > Long.parseLong(request.toNumber())) {
            throw new ValidationException(
                    "From number must not exceed to number");
        }
        final var entity = NumberRangeMapper.toIntervalEntity(request, numberRange);
        final var saved = intervalRepository.save(entity);
        return NumberRangeMapper.toIntervalDto(saved);
    }

    /**
     * Returns all intervals for a number range.
     *
     * @param rangeId the number range ID
     * @return the list of interval DTOs
     * @throws EntityNotFoundException if the range is not found
     */
    public List<NumberRangeIntervalDto> findIntervals(final Long rangeId) {
        Objects.requireNonNull(rangeId, "rangeId must not be null");
        final var numberRange = numberRangeRepository.findById(rangeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Number range with id %d not found".formatted(rangeId)));
        return intervalRepository.findByNumberRange(numberRange).stream()
                .map(NumberRangeMapper::toIntervalDto)
                .toList();
    }

    /**
     * Assigns the next number from a number range interval.
     *
     * @param rangeObject the range object name
     * @param subObject   the sub-object code
     * @return the next number response
     * @throws EntityNotFoundException if the range or interval is not found
     * @throws ValidationException     if the interval is exhausted
     */
    @Transactional
    public NextNumberResponse getNextNumber(final String rangeObject, final String subObject) {
        Objects.requireNonNull(rangeObject, "rangeObject must not be null");
        Objects.requireNonNull(subObject, "subObject must not be null");
        final var numberRange = numberRangeRepository.findByRangeObject(rangeObject)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Number range '%s' not found".formatted(rangeObject)));
        final var interval = intervalRepository
                .findByNumberRangeAndSubObject(numberRange, subObject)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Interval '%s' not found for range '%s'"
                                .formatted(subObject, rangeObject)));
        final var current = Long.parseLong(interval.getCurrentNumber());
        final var upper = Long.parseLong(interval.getToNumber());
        if (current >= upper) {
            throw new ValidationException(
                    "Number range interval '%s/%s' is exhausted"
                            .formatted(rangeObject, subObject));
        }
        final var next = current + 1;
        interval.setCurrentNumber(String.valueOf(next));
        intervalRepository.save(interval);
        return new NextNumberResponse(rangeObject, subObject, String.valueOf(next));
    }
}
