package com.erp.kernel.navigation.controller;

import com.erp.kernel.navigation.dto.CreateUserPreferenceRequest;
import com.erp.kernel.navigation.dto.UserPreferenceDto;
import com.erp.kernel.navigation.service.UserPreferenceService;
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

import java.util.Objects;

/**
 * REST controller for managing user preferences.
 *
 * <p>Provides CRUD operations for user preferences at
 * {@code /api/v1/navigation/preferences}, enabling management of per-user
 * presentation settings such as theme selection, locale, date/time format,
 * and pagination configuration.
 */
@RestController
@RequestMapping("/api/v1/navigation/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    /**
     * Creates a new user preference controller.
     *
     * @param userPreferenceService the user preference service
     */
    public UserPreferenceController(final UserPreferenceService userPreferenceService) {
        this.userPreferenceService = Objects.requireNonNull(userPreferenceService,
                "userPreferenceService must not be null");
    }

    /**
     * Creates new user preferences.
     *
     * @param request the creation request
     * @return the created user preferences with HTTP 201
     */
    @PostMapping
    public ResponseEntity<UserPreferenceDto> create(
            @Valid @RequestBody final CreateUserPreferenceRequest request) {
        final var created = userPreferenceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves user preferences by ID.
     *
     * @param id the preference ID
     * @return the user preference DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserPreferenceDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(userPreferenceService.findById(id));
    }

    /**
     * Retrieves user preferences by user ID.
     *
     * @param userId the user ID
     * @return the user preference DTO
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserPreferenceDto> findByUserId(@PathVariable final Long userId) {
        return ResponseEntity.ok(userPreferenceService.findByUserId(userId));
    }

    /**
     * Updates existing user preferences.
     *
     * @param id      the preference ID
     * @param request the update request
     * @return the updated user preferences
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserPreferenceDto> update(
            @PathVariable final Long id,
            @Valid @RequestBody final CreateUserPreferenceRequest request) {
        return ResponseEntity.ok(userPreferenceService.update(id, request));
    }

    /**
     * Deletes user preferences by ID.
     *
     * @param id the preference ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userPreferenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
