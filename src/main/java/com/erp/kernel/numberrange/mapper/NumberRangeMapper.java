package com.erp.kernel.numberrange.mapper;

import com.erp.kernel.numberrange.dto.CreateIntervalRequest;
import com.erp.kernel.numberrange.dto.CreateNumberRangeRequest;
import com.erp.kernel.numberrange.dto.NumberRangeDto;
import com.erp.kernel.numberrange.dto.NumberRangeIntervalDto;
import com.erp.kernel.numberrange.entity.NumberRange;
import com.erp.kernel.numberrange.entity.NumberRangeInterval;

import java.util.Objects;

/**
 * Maps between number range entities and their DTOs.
 */
public final class NumberRangeMapper {

    private NumberRangeMapper() {
    }

    /**
     * Converts a number range entity to its response DTO.
     *
     * @param entity the number range entity
     * @return the number range DTO
     */
    public static NumberRangeDto toDto(final NumberRange entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new NumberRangeDto(
                entity.getId(),
                entity.getRangeObject(),
                entity.getDescription(),
                entity.isBuffered(),
                entity.getBufferSize(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new number range entity.
     *
     * @param request the create request
     * @return a new number range entity (not yet persisted)
     */
    public static NumberRange toEntity(final CreateNumberRangeRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var entity = new NumberRange();
        entity.setRangeObject(request.rangeObject());
        entity.setDescription(request.description());
        entity.setBuffered(request.buffered());
        entity.setBufferSize(request.bufferSize());
        return entity;
    }

    /**
     * Updates an existing number range entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     */
    public static void updateEntity(final NumberRange entity,
                                    final CreateNumberRangeRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setRangeObject(request.rangeObject());
        entity.setDescription(request.description());
        entity.setBuffered(request.buffered());
        entity.setBufferSize(request.bufferSize());
    }

    /**
     * Converts a number range interval entity to its response DTO.
     *
     * @param entity the interval entity
     * @return the interval DTO
     */
    public static NumberRangeIntervalDto toIntervalDto(final NumberRangeInterval entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new NumberRangeIntervalDto(
                entity.getId(),
                entity.getNumberRange().getId(),
                entity.getSubObject(),
                entity.getFromNumber(),
                entity.getToNumber(),
                entity.getCurrentNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create interval request to a new interval entity.
     *
     * @param request     the create request
     * @param numberRange the parent number range
     * @return a new interval entity (not yet persisted)
     */
    public static NumberRangeInterval toIntervalEntity(final CreateIntervalRequest request,
                                                       final NumberRange numberRange) {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(numberRange, "numberRange must not be null");
        final var entity = new NumberRangeInterval();
        entity.setNumberRange(numberRange);
        entity.setSubObject(request.subObject());
        entity.setFromNumber(request.fromNumber());
        entity.setToNumber(request.toNumber());
        entity.setCurrentNumber(request.fromNumber());
        return entity;
    }
}
