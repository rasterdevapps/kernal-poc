package com.erp.kernel.ddic.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link SearchHelp} entity.
 */
class SearchHelpTest {

    @Test
    void shouldSetAndGetSearchHelpName() {
        final var searchHelp = new SearchHelp();
        searchHelp.setSearchHelpName("SH_CUSTOMER");
        assertThat(searchHelp.getSearchHelpName()).isEqualTo("SH_CUSTOMER");
    }

    @Test
    void shouldSetAndGetTableDefinition() {
        final var table = new TableDefinition();
        table.setTableName("CUSTOMERS");
        final var searchHelp = new SearchHelp();
        searchHelp.setTableDefinition(table);
        assertThat(searchHelp.getTableDefinition()).isEqualTo(table);
    }

    @Test
    void shouldSetAndGetDescription() {
        final var searchHelp = new SearchHelp();
        searchHelp.setDescription("Customer search help");
        assertThat(searchHelp.getDescription()).isEqualTo("Customer search help");
    }
}
