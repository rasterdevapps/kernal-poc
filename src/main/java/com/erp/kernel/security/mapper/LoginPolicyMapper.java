package com.erp.kernel.security.mapper;

import com.erp.kernel.security.dto.CreateLoginPolicyRequest;
import com.erp.kernel.security.dto.LoginPolicyDto;
import com.erp.kernel.security.entity.LoginPolicy;

import java.util.Objects;

/**
 * Maps between {@link LoginPolicy} entities and their DTOs.
 */
public final class LoginPolicyMapper {

    private LoginPolicyMapper() {
    }

    /**
     * Converts a login policy entity to its response DTO.
     *
     * @param entity the login policy entity
     * @return the login policy DTO
     */
    public static LoginPolicyDto toDto(final LoginPolicy entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new LoginPolicyDto(
                entity.getId(),
                entity.getPolicyName(),
                entity.getDescription(),
                entity.getMinPasswordLength(),
                entity.isRequireUppercase(),
                entity.isRequireLowercase(),
                entity.isRequireDigit(),
                entity.isRequireSpecialChar(),
                entity.getPasswordExpiryDays(),
                entity.getMaxFailedAttempts(),
                entity.getLockoutDurationMinutes(),
                entity.getSessionTimeoutMinutes(),
                entity.isEnforceMfa(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new login policy entity.
     *
     * @param request the create request
     * @return a new login policy entity (not yet persisted)
     */
    public static LoginPolicy toEntity(final CreateLoginPolicyRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var policy = new LoginPolicy();
        policy.setPolicyName(request.policyName());
        policy.setDescription(request.description());
        policy.setMinPasswordLength(request.minPasswordLength());
        policy.setRequireUppercase(request.requireUppercase());
        policy.setRequireLowercase(request.requireLowercase());
        policy.setRequireDigit(request.requireDigit());
        policy.setRequireSpecialChar(request.requireSpecialChar());
        policy.setPasswordExpiryDays(request.passwordExpiryDays());
        policy.setMaxFailedAttempts(request.maxFailedAttempts());
        policy.setLockoutDurationMinutes(request.lockoutDurationMinutes());
        policy.setSessionTimeoutMinutes(request.sessionTimeoutMinutes());
        policy.setEnforceMfa(request.enforceMfa());
        return policy;
    }

    /**
     * Updates an existing login policy entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     */
    public static void updateEntity(final LoginPolicy entity, final CreateLoginPolicyRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setPolicyName(request.policyName());
        entity.setDescription(request.description());
        entity.setMinPasswordLength(request.minPasswordLength());
        entity.setRequireUppercase(request.requireUppercase());
        entity.setRequireLowercase(request.requireLowercase());
        entity.setRequireDigit(request.requireDigit());
        entity.setRequireSpecialChar(request.requireSpecialChar());
        entity.setPasswordExpiryDays(request.passwordExpiryDays());
        entity.setMaxFailedAttempts(request.maxFailedAttempts());
        entity.setLockoutDurationMinutes(request.lockoutDurationMinutes());
        entity.setSessionTimeoutMinutes(request.sessionTimeoutMinutes());
        entity.setEnforceMfa(request.enforceMfa());
    }
}
