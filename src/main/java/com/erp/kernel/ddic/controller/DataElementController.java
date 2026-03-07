package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateDataElementRequest;
import com.erp.kernel.ddic.dto.DataElementDto;
import com.erp.kernel.ddic.service.DataElementService;
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
 * REST controller for managing DDIC data elements.
 *
 * <p>Provides CRUD operations for data elements at {@code /api/v1/ddic/data-elements}.
 */
@RestController
@RequestMapping("/api/v1/ddic/data-elements")
public class DataElementController {

    private final DataElementService dataElementService;

    /**
     * Creates a new data element controller.
     *
     * @param dataElementService the data element service
     */
    public DataElementController(final DataElementService dataElementService) {
        this.dataElementService = Objects.requireNonNull(dataElementService,
                "dataElementService must not be null");
    }

    /**
     * Creates a new data element.
     *
     * @param request the creation request
     * @return the created data element with HTTP 201
     */
    @PostMapping
    public ResponseEntity<DataElementDto> create(
            @Valid @RequestBody final CreateDataElementRequest request) {
        final var created = dataElementService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a data element by its ID.
     *
     * @param id the data element ID
     * @return the data element DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataElementDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(dataElementService.findById(id));
    }

    /**
     * Retrieves all data elements.
     *
     * @return the list of all data elements
     */
    @GetMapping
    public ResponseEntity<List<DataElementDto>> findAll() {
        return ResponseEntity.ok(dataElementService.findAll());
    }

    /**
     * Updates an existing data element.
     *
     * @param id      the data element ID
     * @param request the update request
     * @return the updated data element
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataElementDto> update(@PathVariable final Long id,
                                                 @Valid @RequestBody final CreateDataElementRequest request) {
        return ResponseEntity.ok(dataElementService.update(id, request));
    }

    /**
     * Deletes a data element by its ID.
     *
     * @param id the data element ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        dataElementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
