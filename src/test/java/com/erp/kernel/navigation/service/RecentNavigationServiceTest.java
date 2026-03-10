package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateRecentNavigationRequest;
import com.erp.kernel.navigation.entity.RecentNavigation;
import com.erp.kernel.navigation.repository.RecentNavigationRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link RecentNavigationService}.
 */
@ExtendWith(MockitoExtension.class)
class RecentNavigationServiceTest {

    @Mock
    private RecentNavigationRepository recentNavigationRepository;

    @InjectMocks
    private RecentNavigationService recentNavigationService;

    @Test
    void shouldRecordNavigation() {
        final var request = new CreateRecentNavigationRequest(1L, 2L);
        when(recentNavigationRepository.save(any(RecentNavigation.class))).thenAnswer(invocation -> {
            final var saved = invocation.<RecentNavigation>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = recentNavigationService.record(request);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.tcodeId()).isEqualTo(2L);
        verify(recentNavigationRepository).save(any(RecentNavigation.class));
    }

    @Test
    void shouldReturnNavigation_whenFoundById() {
        final var entity = createEntity(1L, 10L, 20L);
        when(recentNavigationRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = recentNavigationService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.userId()).isEqualTo(10L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenNavigationNotFoundById() {
        when(recentNavigationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recentNavigationService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnNavigationsForUser() {
        when(recentNavigationRepository.findByUserIdOrderByAccessedAtDesc(1L)).thenReturn(List.of(
                createEntity(1L, 1L, 10L), createEntity(2L, 1L, 20L)));

        final var result = recentNavigationService.findByUserId(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldDeleteNavigationsForUser() {
        recentNavigationService.deleteByUserId(1L);

        verify(recentNavigationRepository).deleteByUserId(1L);
    }

    private RecentNavigation createEntity(final Long id, final Long userId, final Long tcodeId) {
        final var entry = new RecentNavigation();
        entry.setId(id);
        entry.setUserId(userId);
        entry.setTcodeId(tcodeId);
        entry.setAccessedAt(Instant.now());
        entry.setCreatedAt(Instant.now());
        entry.setUpdatedAt(Instant.now());
        return entry;
    }
}
