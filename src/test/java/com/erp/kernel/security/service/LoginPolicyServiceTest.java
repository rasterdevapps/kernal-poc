package com.erp.kernel.security.service;

import com.erp.kernel.security.dto.CreateLoginPolicyRequest;
import com.erp.kernel.security.entity.LoginPolicy;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.repository.LoginPolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link LoginPolicyService}.
 */
@ExtendWith(MockitoExtension.class)
class LoginPolicyServiceTest {

    @Mock
    private LoginPolicyRepository loginPolicyRepository;

    @InjectMocks
    private LoginPolicyService loginPolicyService;

    @Test
    void shouldCreatePolicy_whenNameIsUnique() {
        final var request = createRequest("DEFAULT_POLICY");
        when(loginPolicyRepository.existsByPolicyName("DEFAULT_POLICY")).thenReturn(false);
        when(loginPolicyRepository.save(any(LoginPolicy.class))).thenAnswer(invocation -> {
            final var saved = invocation.<LoginPolicy>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = loginPolicyService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.policyName()).isEqualTo("DEFAULT_POLICY");
        verify(loginPolicyRepository).save(any(LoginPolicy.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenPolicyNameExists() {
        final var request = createRequest("DEFAULT_POLICY");
        when(loginPolicyRepository.existsByPolicyName("DEFAULT_POLICY")).thenReturn(true);

        assertThatThrownBy(() -> loginPolicyService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("DEFAULT_POLICY");
        verify(loginPolicyRepository, never()).save(any());
    }

    @Test
    void shouldReturnPolicy_whenFoundById() {
        final var entity = createPolicy(1L, "DEFAULT");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = loginPolicyService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.policyName()).isEqualTo("DEFAULT");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenPolicyNotFoundById() {
        when(loginPolicyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginPolicyService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnAllPolicies() {
        when(loginPolicyRepository.findAll()).thenReturn(List.of(
                createPolicy(1L, "STRICT"), createPolicy(2L, "DEFAULT")));

        final var result = loginPolicyService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdatePolicy_whenIdExistsAndNameIsUnique() {
        final var entity = createPolicy(1L, "OLD_NAME");
        final var request = createRequest("NEW_NAME");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(loginPolicyRepository.findByPolicyName("NEW_NAME")).thenReturn(Optional.empty());
        when(loginPolicyRepository.save(any(LoginPolicy.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = loginPolicyService.update(1L, request);

        assertThat(result.policyName()).isEqualTo("NEW_NAME");
    }

    @Test
    void shouldUpdatePolicy_whenNameUnchanged() {
        final var entity = createPolicy(1L, "SAME_NAME");
        final var request = createRequest("SAME_NAME");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(loginPolicyRepository.findByPolicyName("SAME_NAME")).thenReturn(Optional.of(entity));
        when(loginPolicyRepository.save(any(LoginPolicy.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = loginPolicyService.update(1L, request);

        assertThat(result.policyName()).isEqualTo("SAME_NAME");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentPolicy() {
        final var request = createRequest("NAME");
        when(loginPolicyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginPolicyService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateNameConflicts() {
        final var entity = createPolicy(1L, "ORIGINAL");
        final var conflicting = createPolicy(2L, "TAKEN");
        final var request = createRequest("TAKEN");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(loginPolicyRepository.findByPolicyName("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> loginPolicyService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("TAKEN");
    }

    @Test
    void shouldDeletePolicy_whenIdExists() {
        when(loginPolicyRepository.existsById(1L)).thenReturn(true);

        loginPolicyService.delete(1L);

        verify(loginPolicyRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentPolicy() {
        when(loginPolicyRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> loginPolicyService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldValidatePassword_whenAllRequirementsMet() {
        final var policy = createPolicy(1L, "STRICT");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThat(loginPolicyService.validatePassword("Pass123!", 1L)).isTrue();
    }

    @Test
    void shouldRejectPassword_whenTooShort() {
        final var policy = createPolicy(1L, "STRICT");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThat(loginPolicyService.validatePassword("Pa1!", 1L)).isFalse();
    }

    @Test
    void shouldRejectPassword_whenNoUppercase() {
        final var policy = createPolicy(1L, "STRICT");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThat(loginPolicyService.validatePassword("password1!", 1L)).isFalse();
    }

    @Test
    void shouldRejectPassword_whenNoLowercase() {
        final var policy = createPolicy(1L, "STRICT");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThat(loginPolicyService.validatePassword("PASSWORD1!", 1L)).isFalse();
    }

    @Test
    void shouldRejectPassword_whenNoDigit() {
        final var policy = createPolicy(1L, "STRICT");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThat(loginPolicyService.validatePassword("Password!", 1L)).isFalse();
    }

    @Test
    void shouldRejectPassword_whenNoSpecialChar() {
        final var policy = createPolicy(1L, "STRICT");
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThat(loginPolicyService.validatePassword("Password1", 1L)).isFalse();
    }

    @Test
    void shouldThrowEntityNotFoundException_whenValidatingWithNonExistentPolicy() {
        when(loginPolicyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginPolicyService.validatePassword("Pass123!", 99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldAcceptPassword_whenRequirementsAreDisabled() {
        final var policy = createPolicy(1L, "RELAXED");
        policy.setRequireUppercase(false);
        policy.setRequireLowercase(false);
        policy.setRequireDigit(false);
        policy.setRequireSpecialChar(false);
        policy.setMinPasswordLength(1);
        when(loginPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThat(loginPolicyService.validatePassword("a", 1L)).isTrue();
    }

    private CreateLoginPolicyRequest createRequest(final String name) {
        return new CreateLoginPolicyRequest(name, "Test policy",
                8, true, true, true, true, 90, 5, 30, 480, false);
    }

    private LoginPolicy createPolicy(final Long id, final String name) {
        final var policy = new LoginPolicy();
        policy.setId(id);
        policy.setPolicyName(name);
        policy.setDescription("Test policy");
        policy.setMinPasswordLength(8);
        policy.setRequireUppercase(true);
        policy.setRequireLowercase(true);
        policy.setRequireDigit(true);
        policy.setRequireSpecialChar(true);
        policy.setPasswordExpiryDays(90);
        policy.setMaxFailedAttempts(5);
        policy.setLockoutDurationMinutes(30);
        policy.setSessionTimeoutMinutes(480);
        policy.setActive(true);
        policy.setCreatedAt(Instant.now());
        policy.setUpdatedAt(Instant.now());
        return policy;
    }
}
