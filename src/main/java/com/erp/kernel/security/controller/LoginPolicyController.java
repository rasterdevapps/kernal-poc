package com.erp.kernel.security.controller;

import com.erp.kernel.security.dto.CreateLoginPolicyRequest;
import com.erp.kernel.security.dto.LoginPolicyDto;
import com.erp.kernel.security.service.LoginPolicyService;
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
 * REST controller for managing login policies.
 *
 * <p>Provides CRUD operations for login policies at
 * {@code /api/v1/security/login-policies}.
 */
@RestController
@RequestMapping("/api/v1/security/login-policies")
public class LoginPolicyController {

    private final LoginPolicyService loginPolicyService;

    /**
     * Creates a new login policy controller.
     *
     * @param loginPolicyService the login policy service
     */
    public LoginPolicyController(final LoginPolicyService loginPolicyService) {
        this.loginPolicyService = Objects.requireNonNull(loginPolicyService,
                "loginPolicyService must not be null");
    }

    /**
     * Creates a new login policy.
     *
     * @param request the creation request
     * @return the created login policy with HTTP 201
     */
    @PostMapping
    public ResponseEntity<LoginPolicyDto> create(
            @Valid @RequestBody final CreateLoginPolicyRequest request) {
        final var created = loginPolicyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a login policy by ID.
     *
     * @param id the policy ID
     * @return the login policy DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoginPolicyDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(loginPolicyService.findById(id));
    }

    /**
     * Retrieves all login policies.
     *
     * @return the list of all login policies
     */
    @GetMapping
    public ResponseEntity<List<LoginPolicyDto>> findAll() {
        return ResponseEntity.ok(loginPolicyService.findAll());
    }

    /**
     * Updates an existing login policy.
     *
     * @param id      the policy ID
     * @param request the update request
     * @return the updated login policy
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoginPolicyDto> update(@PathVariable final Long id,
                                                 @Valid @RequestBody final CreateLoginPolicyRequest request) {
        return ResponseEntity.ok(loginPolicyService.update(id, request));
    }

    /**
     * Deletes a login policy by ID.
     *
     * @param id the policy ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        loginPolicyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
