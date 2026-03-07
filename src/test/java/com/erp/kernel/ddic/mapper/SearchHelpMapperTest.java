package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateSearchHelpRequest;
import com.erp.kernel.ddic.entity.SearchHelp;
import com.erp.kernel.ddic.entity.TableDefinition;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link SearchHelpMapper}.
 */
class SearchHelpMapperTest {

    @Test
    void shouldConvertEntityToDto() {
        final var table = createTableDefinition(1L, "CUSTOMERS");
        final var entity = createSearchHelpEntity(2L, "SH_CUST", table, "Customer search");
        final var dto = SearchHelpMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.searchHelpName()).isEqualTo("SH_CUST");
        assertThat(dto.tableDefinitionId()).isEqualTo(1L);
        assertThat(dto.tableName()).isEqualTo("CUSTOMERS");
        assertThat(dto.description()).isEqualTo("Customer search");
    }

    @Test
    void shouldConvertRequestToEntity() {
        final var table = createTableDefinition(1L, "ORDERS");
        final var request = new CreateSearchHelpRequest("SH_ORDER", 1L, "Order search");
        final var entity = SearchHelpMapper.toEntity(request, table);

        assertThat(entity.getSearchHelpName()).isEqualTo("SH_ORDER");
        assertThat(entity.getTableDefinition()).isEqualTo(table);
        assertThat(entity.getDescription()).isEqualTo("Order search");
    }

    @Test
    void shouldUpdateExistingEntity() {
        final var oldTable = createTableDefinition(1L, "OLD_TABLE");
        final var newTable = createTableDefinition(2L, "NEW_TABLE");
        final var entity = createSearchHelpEntity(3L, "OLD_SH", oldTable, "Old desc");
        final var request = new CreateSearchHelpRequest("NEW_SH", 2L, "New desc");
        SearchHelpMapper.updateEntity(entity, request, newTable);

        assertThat(entity.getSearchHelpName()).isEqualTo("NEW_SH");
        assertThat(entity.getTableDefinition()).isEqualTo(newTable);
        assertThat(entity.getDescription()).isEqualTo("New desc");
    }

    @Test
    void shouldThrowNullPointerException_whenToDtoEntityIsNull() {
        assertThatThrownBy(() -> SearchHelpMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityRequestIsNull() {
        assertThatThrownBy(() -> SearchHelpMapper.toEntity(null, new TableDefinition()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityTableIsNull() {
        final var request = new CreateSearchHelpRequest("SH", 1L, null);
        assertThatThrownBy(() -> SearchHelpMapper.toEntity(request, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateEntityIsNull() {
        final var request = new CreateSearchHelpRequest("SH", 1L, null);
        assertThatThrownBy(() -> SearchHelpMapper.updateEntity(null, request, new TableDefinition()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateRequestIsNull() {
        assertThatThrownBy(() -> SearchHelpMapper.updateEntity(new SearchHelp(), null, new TableDefinition()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateTableIsNull() {
        final var request = new CreateSearchHelpRequest("SH", 1L, null);
        assertThatThrownBy(() -> SearchHelpMapper.updateEntity(new SearchHelp(), request, null))
                .isInstanceOf(NullPointerException.class);
    }

    private TableDefinition createTableDefinition(final Long id, final String name) {
        final var table = new TableDefinition();
        table.setId(id);
        table.setTableName(name);
        table.setCreatedAt(Instant.now());
        table.setUpdatedAt(Instant.now());
        return table;
    }

    private SearchHelp createSearchHelpEntity(final Long id, final String name,
                                               final TableDefinition table, final String desc) {
        final var searchHelp = new SearchHelp();
        searchHelp.setId(id);
        searchHelp.setSearchHelpName(name);
        searchHelp.setTableDefinition(table);
        searchHelp.setDescription(desc);
        searchHelp.setCreatedAt(Instant.now());
        searchHelp.setUpdatedAt(Instant.now());
        return searchHelp;
    }
}
