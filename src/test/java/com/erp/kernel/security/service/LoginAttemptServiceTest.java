package com.erp.kernel.security.service;

import com.erp.kernel.security.repository.LoginAttemptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link LoginAttemptService}.
 */
@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @Test
    void shouldRecordSuccessfulAttempt() {
        when(loginAttemptRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        loginAttemptService.recordAttempt("user", true, "127.0.0.1", "Mozilla", null);

        verify(loginAttemptRepository).save(any());
    }

    @Test
    void shouldRecordFailedAttempt() {
        when(loginAttemptRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        loginAttemptService.recordAttempt("user", false, "10.0.0.1", "Chrome", "Invalid password");

        verify(loginAttemptRepository).save(any());
    }

    @Test
    void shouldCountRecentFailedAttempts() {
        when(loginAttemptRepository.countByUsernameAndSuccessAndAttemptedAtAfter(
                eq("user"), eq(false), any())).thenReturn(3L);

        final var count = loginAttemptService.countRecentFailedAttempts("user", Duration.ofMinutes(30));

        assertThat(count).isEqualTo(3L);
    }
}
