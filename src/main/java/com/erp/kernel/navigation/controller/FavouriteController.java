package com.erp.kernel.navigation.controller;

import com.erp.kernel.navigation.dto.CreateFavouriteRequest;
import com.erp.kernel.navigation.dto.FavouriteDto;
import com.erp.kernel.navigation.service.FavouriteService;
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
 * REST controller for managing user favourites.
 *
 * <p>Provides operations for managing favourite T-Codes at
 * {@code /api/v1/navigation/favourites}, allowing users to bookmark
 * frequently used transaction codes for quick access.
 */
@RestController
@RequestMapping("/api/v1/navigation/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;

    /**
     * Creates a new favourite controller.
     *
     * @param favouriteService the favourite service
     */
    public FavouriteController(final FavouriteService favouriteService) {
        this.favouriteService = Objects.requireNonNull(favouriteService,
                "favouriteService must not be null");
    }

    /**
     * Creates a new favourite.
     *
     * @param request the creation request
     * @return the created favourite with HTTP 201
     */
    @PostMapping
    public ResponseEntity<FavouriteDto> create(
            @Valid @RequestBody final CreateFavouriteRequest request) {
        final var created = favouriteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a favourite by ID.
     *
     * @param id the favourite ID
     * @return the favourite DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<FavouriteDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(favouriteService.findById(id));
    }

    /**
     * Retrieves all favourites for a specific user.
     *
     * @param userId the user ID
     * @return the list of favourites for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavouriteDto>> findByUserId(@PathVariable final Long userId) {
        return ResponseEntity.ok(favouriteService.findByUserId(userId));
    }

    /**
     * Deletes a favourite by ID.
     *
     * @param id the favourite ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        favouriteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
