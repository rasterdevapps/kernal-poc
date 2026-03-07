package com.erp.kernel.security.service;

import com.erp.kernel.security.dto.CreateLoginPolicyRequest;
import com.erp.kernel.security.dto.LoginPolicyDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.mapper.LoginPolicyMapper;
import com.erp.kernel.security.repository.LoginPolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for login policy management.
 *
 * <p>Manages policies defining password complexity, expiry, lockout,
 * session timeout, and MFA enforcement at various levels.
 */
@Service
@Transactional(readOnly = true)
public class LoginPolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(LoginPolicyService.class);

    private final LoginPolicyRepository loginPolicyRepository;

    /**
     * Creates a new login policy service.
     *
     * @param loginPolicyRepository the login policy repository
     */
    public LoginPolicyService(final LoginPolicyRepository loginPolicyRepository) {
        this.loginPolicyRepository = Objects.requireNonNull(loginPolicyRepository,
                "loginPolicyRepository must not be null");
    }

    /**
     * Creates a new login policy.
     *
     * @param request the creation request
     * @return the created login policy DTO
     * @throws DuplicateEntityException if a policy with the same name exists
     */
    @Transactional
    @CacheEvict(value = "loginPolicies", allEntries = true)
    public LoginPolicyDto create(final CreateLoginPolicyRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (loginPolicyRepository.existsByPolicyName(request.policyName())) {
            throw new DuplicateEntityException(
                    "Login policy with name '%s' already exists".formatted(request.policyName()));
        }
        final var entity = LoginPolicyMapper.toEntity(request);
        final var saved = loginPolicyRepository.save(entity);
        LOG.info("Login policy '{}' created with ID {}", saved.getPolicyName(), saved.getId());
        return LoginPolicyMapper.toDto(saved);
    }

    /**
     * Finds a login policy by ID.
     *
     * @param id the policy ID
     * @return the login policy DTO
     * @throws EntityNotFoundException if the policy is not found
     */
    @Cacheable(value = "loginPolicies", key = "#id")
    public LoginPolicyDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = loginPolicyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Login policy with id %d not found".formatted(id)));
        return LoginPolicyMapper.toDto(entity);
    }

    /**
     * Returns all login policies.
     *
     * @return the list of all login policy DTOs
     */
    @Cacheable(value = "loginPolicies", key = "'all'")
    public List<LoginPolicyDto> findAll() {
        return loginPolicyRepository.findAll().stream()
                .map(LoginPolicyMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing login policy.
     *
     * @param id      the policy ID
     * @param request the update request
     * @return the updated login policy DTO
     * @throws EntityNotFoundException  if the policy is not found
     * @throws DuplicateEntityException if the new name conflicts with another policy
     */
    @Transactional
    @CacheEvict(value = "loginPolicies", allEntries = true)
    public LoginPolicyDto update(final Long id, final CreateLoginPolicyRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = loginPolicyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Login policy with id %d not found".formatted(id)));
        loginPolicyRepository.findByPolicyName(request.policyName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Login policy with name '%s' already exists".formatted(request.policyName()));
                });
        LoginPolicyMapper.updateEntity(entity, request);
        final var saved = loginPolicyRepository.save(entity);
        LOG.info("Login policy '{}' updated", saved.getPolicyName());
        return LoginPolicyMapper.toDto(saved);
    }

    /**
     * Deletes a login policy by ID.
     *
     * @param id the policy ID
     * @throws EntityNotFoundException if the policy is not found
     */
    @Transactional
    @CacheEvict(value = "loginPolicies", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!loginPolicyRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Login policy with id %d not found".formatted(id));
        }
        loginPolicyRepository.deleteById(id);
        LOG.info("Login policy with ID {} deleted", id);
    }

    /**
     * Validates a password against a login policy.
     *
     * @param password the password to validate
     * @param policyId the policy ID
     * @return {@code true} if the password meets the policy requirements
     * @throws EntityNotFoundException if the policy is not found
     */
    public boolean validatePassword(final String password, final Long policyId) {
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(policyId, "policyId must not be null");

        final var policy = loginPolicyRepository.findById(policyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Login policy with id %d not found".formatted(policyId)));

        if (password.length() < policy.getMinPasswordLength()) {
            return false;
        }
        if (policy.isRequireUppercase() && password.equals(password.toLowerCase())) {
            return false;
        }
        if (policy.isRequireLowercase() && password.equals(password.toUpperCase())) {
            return false;
        }
        if (policy.isRequireDigit() && !password.chars().anyMatch(Character::isDigit)) {
            return false;
        }
        return !policy.isRequireSpecialChar()
                || password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
    }
}
