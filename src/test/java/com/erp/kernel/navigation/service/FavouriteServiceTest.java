package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateFavouriteRequest;
import com.erp.kernel.navigation.entity.Favourite;
import com.erp.kernel.navigation.repository.FavouriteRepository;
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
 * Tests for the {@link FavouriteService}.
 */
@ExtendWith(MockitoExtension.class)
class FavouriteServiceTest {

    @Mock
    private FavouriteRepository favouriteRepository;

    @InjectMocks
    private FavouriteService favouriteService;

    @Test
    void shouldCreateFavourite_whenNotExists() {
        final var request = new CreateFavouriteRequest(1L, 2L, 0);
        when(favouriteRepository.existsByUserIdAndTcodeId(1L, 2L)).thenReturn(false);
        when(favouriteRepository.save(any(Favourite.class))).thenAnswer(invocation -> {
            final var saved = invocation.<Favourite>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = favouriteService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.tcodeId()).isEqualTo(2L);
        verify(favouriteRepository).save(any(Favourite.class));
    }

    @Test
    void shouldThrowDuplicateEntityException_whenFavouriteExists() {
        final var request = new CreateFavouriteRequest(1L, 2L, 0);
        when(favouriteRepository.existsByUserIdAndTcodeId(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> favouriteService.create(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("1")
                .hasMessageContaining("2");
        verify(favouriteRepository, never()).save(any());
    }

    @Test
    void shouldReturnFavourite_whenFoundById() {
        final var entity = createEntity(1L, 10L, 20L);
        when(favouriteRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = favouriteService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.userId()).isEqualTo(10L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenFavouriteNotFoundById() {
        when(favouriteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favouriteService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldReturnFavouritesForUser() {
        when(favouriteRepository.findByUserIdOrderBySortOrderAsc(1L)).thenReturn(List.of(
                createEntity(1L, 1L, 10L), createEntity(2L, 1L, 20L)));

        final var result = favouriteService.findByUserId(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldDeleteFavourite_whenIdExists() {
        when(favouriteRepository.existsById(1L)).thenReturn(true);

        favouriteService.delete(1L);

        verify(favouriteRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentFavourite() {
        when(favouriteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> favouriteService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    private Favourite createEntity(final Long id, final Long userId, final Long tcodeId) {
        final var favourite = new Favourite();
        favourite.setId(id);
        favourite.setUserId(userId);
        favourite.setTcodeId(tcodeId);
        favourite.setSortOrder(0);
        favourite.setCreatedAt(Instant.now());
        favourite.setUpdatedAt(Instant.now());
        return favourite;
    }
}
