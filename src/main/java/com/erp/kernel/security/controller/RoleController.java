package com.erp.kernel.security.controller;

import com.erp.kernel.security.dto.CreateRoleRequest;
import com.erp.kernel.security.dto.RoleDto;
import com.erp.kernel.security.service.RoleService;
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
 * REST controller for managing roles.
 *
 * <p>Provides CRUD operations for roles at {@code /api/v1/security/roles},
 * similar to SAP PFCG role maintenance.
 */
@RestController
@RequestMapping("/api/v1/security/roles")
public class RoleController {

    private final RoleService roleService;

    /**
     * Creates a new role controller.
     *
     * @param roleService the role service
     */
    public RoleController(final RoleService roleService) {
        this.roleService = Objects.requireNonNull(roleService, "roleService must not be null");
    }

    /**
     * Creates a new role.
     *
     * @param request the creation request
     * @return the created role with HTTP 201
     */
    @PostMapping
    public ResponseEntity<RoleDto> create(@Valid @RequestBody final CreateRoleRequest request) {
        final var created = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a role by ID.
     *
     * @param id the role ID
     * @return the role DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    /**
     * Retrieves all roles.
     *
     * @return the list of all roles
     */
    @GetMapping
    public ResponseEntity<List<RoleDto>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    /**
     * Updates an existing role.
     *
     * @param id      the role ID
     * @param request the update request
     * @return the updated role
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> update(@PathVariable final Long id,
                                          @Valid @RequestBody final CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.update(id, request));
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the role ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
