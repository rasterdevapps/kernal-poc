package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateFavouriteRequest;
import com.erp.kernel.navigation.dto.FavouriteDto;
import com.erp.kernel.navigation.entity.Favourite;

import java.util.Objects;

/**
 * Mapper for converting between {@link Favourite} entities and DTOs.
 */
public final class FavouriteMapper {

    private FavouriteMapper() {
    }

    /**
     * Converts a favourite entity to its response DTO.
     *
     * @param entity the favourite entity
     * @return the favourite DTO
     */
    public static FavouriteDto toDto(final Favourite entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new FavouriteDto(
                entity.getId(),
                entity.getUserId(),
                entity.getTcodeId(),
                entity.getSortOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new favourite entity.
     *
     * @param request the create request
     * @return the favourite entity
     */
    public static Favourite toEntity(final CreateFavouriteRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var favourite = new Favourite();
        favourite.setUserId(request.userId());
        favourite.setTcodeId(request.tcodeId());
        favourite.setSortOrder(request.sortOrder());
        return favourite;
    }

    /**
     * Updates an existing favourite entity with values from the request.
     *
     * @param entity  the existing favourite entity
     * @param request the update request
     */
    public static void updateEntity(final Favourite entity, final CreateFavouriteRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setSortOrder(request.sortOrder());
    }
}
