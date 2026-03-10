package com.erp.kernel.navigation.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for recording a recent navigation entry.
 *
 * @param userId  the user ID
 * @param tcodeId the T-code ID
 */
public record CreateRecentNavigationRequest(
        @NotNull Long userId,
        @NotNull Long tcodeId
) {
}
