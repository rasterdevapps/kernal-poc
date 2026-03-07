package com.erp.kernel.security.controller;

import com.erp.kernel.security.dto.CreateUserRequest;
import com.erp.kernel.security.dto.UserDto;
import com.erp.kernel.security.service.UserService;
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
 * REST controller for managing users.
 *
 * <p>Provides CRUD operations for users at {@code /api/v1/security/users},
 * similar to SAP SU01 user administration.
 */
@RestController
@RequestMapping("/api/v1/security/users")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user controller.
     *
     * @param userService the user service
     */
    public UserController(final UserService userService) {
        this.userService = Objects.requireNonNull(userService, "userService must not be null");
    }

    /**
     * Creates a new user.
     *
     * @param request the creation request
     * @return the created user with HTTP 201
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody final CreateUserRequest request) {
        final var created = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Retrieves all users.
     *
     * @return the list of all users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Updates an existing user.
     *
     * @param id      the user ID
     * @param request the update request
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable final Long id,
                                          @Valid @RequestBody final CreateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
