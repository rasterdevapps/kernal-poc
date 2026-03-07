package com.erp.kernel.numberrange.controller;

import com.erp.kernel.numberrange.dto.CreateIntervalRequest;
import com.erp.kernel.numberrange.dto.CreateNumberRangeRequest;
import com.erp.kernel.numberrange.dto.NextNumberResponse;
import com.erp.kernel.numberrange.dto.NumberRangeDto;
import com.erp.kernel.numberrange.dto.NumberRangeIntervalDto;
import com.erp.kernel.numberrange.service.NumberRangeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing number ranges and intervals.
 *
 * <p>Provides CRUD operations for number ranges at {@code /api/v1/number-ranges}
 * and interval management and number assignment endpoints.
 */
@RestController
@RequestMapping("/api/v1/number-ranges")
public class NumberRangeController {

    private final NumberRangeService numberRangeService;

    /**
     * Creates a new number range controller.
     *
     * @param numberRangeService the number range service
     */
    public NumberRangeController(final NumberRangeService numberRangeService) {
        this.numberRangeService = Objects.requireNonNull(
                numberRangeService, "numberRangeService must not be null");
    }

    /**
     * Creates a new number range.
     *
     * @param request the creation request
     * @return the created number range with HTTP 201
     */
    @PostMapping
    public ResponseEntity<NumberRangeDto> create(
            @Valid @RequestBody final CreateNumberRangeRequest request) {
        final var created = numberRangeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a number range by its ID.
     *
     * @param id the number range ID
     * @return the number range DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<NumberRangeDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(numberRangeService.findById(id));
    }

    /**
     * Retrieves all number ranges.
     *
     * @return the list of all number ranges
     */
    @GetMapping
    public ResponseEntity<List<NumberRangeDto>> findAll() {
        return ResponseEntity.ok(numberRangeService.findAll());
    }

    /**
     * Updates an existing number range.
     *
     * @param id      the number range ID
     * @param request the update request
     * @return the updated number range
     */
    @PutMapping("/{id}")
    public ResponseEntity<NumberRangeDto> update(
            @PathVariable final Long id,
            @Valid @RequestBody final CreateNumberRangeRequest request) {
        return ResponseEntity.ok(numberRangeService.update(id, request));
    }

    /**
     * Deletes a number range by its ID.
     *
     * @param id the number range ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        numberRangeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a new interval within a number range.
     *
     * @param rangeId the parent number range ID
     * @param request the interval creation request
     * @return the created interval with HTTP 201
     */
    @PostMapping("/{rangeId}/intervals")
    public ResponseEntity<NumberRangeIntervalDto> createInterval(
            @PathVariable final Long rangeId,
            @Valid @RequestBody final CreateIntervalRequest request) {
        final var created = numberRangeService.createInterval(rangeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves all intervals for a number range.
     *
     * @param rangeId the number range ID
     * @return the list of intervals
     */
    @GetMapping("/{rangeId}/intervals")
    public ResponseEntity<List<NumberRangeIntervalDto>> findIntervals(
            @PathVariable final Long rangeId) {
        return ResponseEntity.ok(numberRangeService.findIntervals(rangeId));
    }

    /**
     * Assigns the next number from a number range interval.
     *
     * @param rangeObject the range object name
     * @param subObject   the sub-object code
     * @return the assigned number
     */
    @PostMapping("/{rangeObject}/next/{subObject}")
    public ResponseEntity<NextNumberResponse> getNextNumber(
            @PathVariable final String rangeObject,
            @PathVariable final String subObject) {
        return ResponseEntity.ok(numberRangeService.getNextNumber(rangeObject, subObject));
    }
}
