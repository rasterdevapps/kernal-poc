package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateRecentNavigationRequest;
import com.erp.kernel.navigation.dto.RecentNavigationDto;
import com.erp.kernel.navigation.entity.RecentNavigation;

import java.time.Instant;
import java.util.Objects;

/**
 * Mapper for converting between {@link RecentNavigation} entities and DTOs.
 */
public final class RecentNavigationMapper {

    private RecentNavigationMapper() {
    }

    /**
     * Converts a recent navigation entity to its response DTO.
     *
     * @param entity the recent navigation entity
     * @return the recent navigation DTO
     */
    public static RecentNavigationDto toDto(final RecentNavigation entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new RecentNavigationDto(
                entity.getId(),
                entity.getUserId(),
                entity.getTcodeId(),
                entity.getAccessedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new recent navigation entity.
     *
     * <p>The {@code accessedAt} timestamp is set to the current instant.
     *
     * @param request the create request
     * @return the recent navigation entity
     */
    public static RecentNavigation toEntity(final CreateRecentNavigationRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var entry = new RecentNavigation();
        entry.setUserId(request.userId());
        entry.setTcodeId(request.tcodeId());
        entry.setAccessedAt(Instant.now());
        return entry;
    }
}
