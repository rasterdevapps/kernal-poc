package com.erp.kernel.security.mapper;

import com.erp.kernel.security.dto.CreateLoginPolicyRequest;
import com.erp.kernel.security.entity.LoginPolicy;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link LoginPolicyMapper}.
 */
class LoginPolicyMapperTest {

    @Test
    void shouldMapEntityToDto() {
        final var entity = createPolicy();

        final var dto = LoginPolicyMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.policyName()).isEqualTo("DEFAULT");
        assertThat(dto.description()).isEqualTo("Default policy");
        assertThat(dto.minPasswordLength()).isEqualTo(8);
        assertThat(dto.requireUppercase()).isTrue();
        assertThat(dto.requireLowercase()).isTrue();
        assertThat(dto.requireDigit()).isTrue();
        assertThat(dto.requireSpecialChar()).isTrue();
        assertThat(dto.passwordExpiryDays()).isEqualTo(90);
        assertThat(dto.maxFailedAttempts()).isEqualTo(5);
        assertThat(dto.lockoutDurationMinutes()).isEqualTo(30);
        assertThat(dto.sessionTimeoutMinutes()).isEqualTo(480);
        assertThat(dto.enforceMfa()).isFalse();
        assertThat(dto.active()).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNull() {
        assertThatThrownBy(() -> LoginPolicyMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldMapRequestToEntity() {
        final var request = new CreateLoginPolicyRequest("STRICT", "Strict policy",
                12, true, true, true, true, 60, 3, 60, 240, true);

        final var entity = LoginPolicyMapper.toEntity(request);

        assertThat(entity.getPolicyName()).isEqualTo("STRICT");
        assertThat(entity.getMinPasswordLength()).isEqualTo(12);
        assertThat(entity.isEnforceMfa()).isTrue();
        assertThat(entity.getMaxFailedAttempts()).isEqualTo(3);
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNullForToEntity() {
        assertThatThrownBy(() -> LoginPolicyMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldUpdateEntity() {
        final var entity = createPolicy();
        final var request = new CreateLoginPolicyRequest("UPDATED", "Updated policy",
                10, false, false, false, false, 30, 10, 15, 120, true);

        LoginPolicyMapper.updateEntity(entity, request);

        assertThat(entity.getPolicyName()).isEqualTo("UPDATED");
        assertThat(entity.getMinPasswordLength()).isEqualTo(10);
        assertThat(entity.isRequireUppercase()).isFalse();
        assertThat(entity.isEnforceMfa()).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNullForUpdate() {
        final var request = new CreateLoginPolicyRequest("N", null, 8, true, true, true, true, 90, 5, 30, 480, false);
        assertThatThrownBy(() -> LoginPolicyMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNullForUpdate() {
        assertThatThrownBy(() -> LoginPolicyMapper.updateEntity(new LoginPolicy(), null))
                .isInstanceOf(NullPointerException.class);
    }

    private LoginPolicy createPolicy() {
        final var policy = new LoginPolicy();
        policy.setId(1L);
        policy.setPolicyName("DEFAULT");
        policy.setDescription("Default policy");
        policy.setMinPasswordLength(8);
        policy.setRequireUppercase(true);
        policy.setRequireLowercase(true);
        policy.setRequireDigit(true);
        policy.setRequireSpecialChar(true);
        policy.setPasswordExpiryDays(90);
        policy.setMaxFailedAttempts(5);
        policy.setLockoutDurationMinutes(30);
        policy.setSessionTimeoutMinutes(480);
        policy.setEnforceMfa(false);
        policy.setActive(true);
        policy.setCreatedAt(Instant.now());
        policy.setUpdatedAt(Instant.now());
        return policy;
    }
}
