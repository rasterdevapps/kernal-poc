package com.erp.kernel.navigation.controller;

import com.erp.kernel.navigation.dto.CreateRecentNavigationRequest;
import com.erp.kernel.navigation.dto.RecentNavigationDto;
import com.erp.kernel.navigation.service.RecentNavigationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing recent navigation history.
 *
 * <p>Provides operations for recording and querying navigation history at
 * {@code /api/v1/navigation/recent}, enabling a "recently visited" list
 * that tracks which T-Codes a user has accessed.
 */
@RestController
@RequestMapping("/api/v1/navigation/recent")
public class RecentNavigationController {

    private final RecentNavigationService recentNavigationService;

    /**
     * Creates a new recent navigation controller.
     *
     * @param recentNavigationService the recent navigation service
     */
    public RecentNavigationController(final RecentNavigationService recentNavigationService) {
        this.recentNavigationService = Objects.requireNonNull(recentNavigationService,
                "recentNavigationService must not be null");
    }

    /**
     * Records a new navigation entry.
     *
     * @param request the creation request
     * @return the recorded navigation entry with HTTP 201
     */
    @PostMapping
    public ResponseEntity<RecentNavigationDto> record(
            @Valid @RequestBody final CreateRecentNavigationRequest request) {
        final var recorded = recentNavigationService.record(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(recorded);
    }

    /**
     * Retrieves a recent navigation entry by ID.
     *
     * @param id the navigation entry ID
     * @return the recent navigation DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecentNavigationDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(recentNavigationService.findById(id));
    }

    /**
     * Retrieves all recent navigation entries for a specific user.
     *
     * @param userId the user ID
     * @return the list of recent navigation entries for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecentNavigationDto>> findByUserId(@PathVariable final Long userId) {
        return ResponseEntity.ok(recentNavigationService.findByUserId(userId));
    }

    /**
     * Deletes all navigation history for a specific user.
     *
     * @param userId the user ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable final Long userId) {
        recentNavigationService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
