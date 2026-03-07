package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateSearchHelpRequest;
import com.erp.kernel.ddic.dto.SearchHelpDto;
import com.erp.kernel.ddic.entity.SearchHelp;
import com.erp.kernel.ddic.entity.TableDefinition;

import java.util.Objects;

/**
 * Maps between {@link SearchHelp} entities and their DTOs.
 */
public final class SearchHelpMapper {

    private SearchHelpMapper() {
    }

    /**
     * Converts a search help entity to its response DTO.
     *
     * @param entity the search help entity
     * @return the search help DTO
     */
    public static SearchHelpDto toDto(final SearchHelp entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new SearchHelpDto(
                entity.getId(),
                entity.getSearchHelpName(),
                entity.getTableDefinition().getId(),
                entity.getTableDefinition().getTableName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new search help entity.
     *
     * @param request         the create request
     * @param tableDefinition the referenced table definition
     * @return a new search help entity (not yet persisted)
     */
    public static SearchHelp toEntity(final CreateSearchHelpRequest request,
                                      final TableDefinition tableDefinition) {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(tableDefinition, "tableDefinition must not be null");
        final var searchHelp = new SearchHelp();
        searchHelp.setSearchHelpName(request.searchHelpName());
        searchHelp.setTableDefinition(tableDefinition);
        searchHelp.setDescription(request.description());
        return searchHelp;
    }

    /**
     * Updates an existing search help entity with values from the request.
     *
     * @param entity          the entity to update
     * @param request         the update request
     * @param tableDefinition the referenced table definition
     */
    public static void updateEntity(final SearchHelp entity,
                                    final CreateSearchHelpRequest request,
                                    final TableDefinition tableDefinition) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(tableDefinition, "tableDefinition must not be null");
        entity.setSearchHelpName(request.searchHelpName());
        entity.setTableDefinition(tableDefinition);
        entity.setDescription(request.description());
    }
}
