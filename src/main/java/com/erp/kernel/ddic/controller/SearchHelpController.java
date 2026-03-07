package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateSearchHelpRequest;
import com.erp.kernel.ddic.dto.SearchHelpDto;
import com.erp.kernel.ddic.service.SearchHelpService;
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
 * REST controller for managing DDIC search helps.
 *
 * <p>Provides CRUD operations for search helps at {@code /api/v1/ddic/search-helps}.
 */
@RestController
@RequestMapping("/api/v1/ddic/search-helps")
public class SearchHelpController {

    private final SearchHelpService searchHelpService;

    /**
     * Creates a new search help controller.
     *
     * @param searchHelpService the search help service
     */
    public SearchHelpController(final SearchHelpService searchHelpService) {
        this.searchHelpService = Objects.requireNonNull(searchHelpService,
                "searchHelpService must not be null");
    }

    /**
     * Creates a new search help.
     *
     * @param request the creation request
     * @return the created search help with HTTP 201
     */
    @PostMapping
    public ResponseEntity<SearchHelpDto> create(
            @Valid @RequestBody final CreateSearchHelpRequest request) {
        final var created = searchHelpService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a search help by its ID.
     *
     * @param id the search help ID
     * @return the search help DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchHelpDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(searchHelpService.findById(id));
    }

    /**
     * Retrieves all search helps.
     *
     * @return the list of all search helps
     */
    @GetMapping
    public ResponseEntity<List<SearchHelpDto>> findAll() {
        return ResponseEntity.ok(searchHelpService.findAll());
    }

    /**
     * Updates an existing search help.
     *
     * @param id      the search help ID
     * @param request the update request
     * @return the updated search help
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchHelpDto> update(@PathVariable final Long id,
                                                @Valid @RequestBody final CreateSearchHelpRequest request) {
        return ResponseEntity.ok(searchHelpService.update(id, request));
    }

    /**
     * Deletes a search help by its ID.
     *
     * @param id the search help ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        searchHelpService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
