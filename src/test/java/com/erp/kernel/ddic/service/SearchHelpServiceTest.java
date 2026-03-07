package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateSearchHelpRequest;
import com.erp.kernel.ddic.entity.SearchHelp;
import com.erp.kernel.ddic.entity.TableDefinition;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.repository.SearchHelpRepository;
import com.erp.kernel.ddic.repository.TableDefinitionRepository;
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
 * Tests for the {@link SearchHelpService}.
 */
@ExtendWith(MockitoExtension.class)
class SearchHelpServiceTest {

    @Mock
    private SearchHelpRepository searchHelpRepository;

    @Mock
    private TableDefinitionRepository tableDefinitionRepository;

    @InjectMocks
    private SearchHelpService searchHelpService;

    @Test
    void shouldCreateSearchHelp_whenNameIsUnique() {
        final var request = new CreateSearchHelpRequest("SH_CUST", 1L, "Customer search");
        final var table = createTableDefinition(1L);
        when(searchHelpRepository.existsBySearchHelpName("SH_CUST")).thenReturn(false);
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(table));
        when(searchHelpRepository.save(any(SearchHelp.class))).thenAnswer(invocation -> {
            final var saved = invocation.<SearchHelp>getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());
            return saved;
        });

        final var result = searchHelpService.create(request);

        assertThat(result.searchHelpName()).isEqualTo("SH_CUST");
    }

    @Test
    void shouldThrowDuplicateEntityException_whenSearchHelpNameExists() {
        final var request = new CreateSearchHelpRequest("SH_CUST", 1L, null);
        when(searchHelpRepository.existsBySearchHelpName("SH_CUST")).thenReturn(true);

        assertThatThrownBy(() -> searchHelpService.create(request))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenTableNotFoundOnCreate() {
        final var request = new CreateSearchHelpRequest("SH_CUST", 99L, null);
        when(searchHelpRepository.existsBySearchHelpName("SH_CUST")).thenReturn(false);
        when(tableDefinitionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> searchHelpService.create(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnSearchHelp_whenFoundById() {
        final var entity = createSearchHelp(1L, "SH_ORDER");
        when(searchHelpRepository.findById(1L)).thenReturn(Optional.of(entity));

        final var result = searchHelpService.findById(1L);

        assertThat(result.searchHelpName()).isEqualTo("SH_ORDER");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenSearchHelpNotFoundById() {
        when(searchHelpRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> searchHelpService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnAllSearchHelps() {
        when(searchHelpRepository.findAll()).thenReturn(List.of(
                createSearchHelp(1L, "SH_1"), createSearchHelp(2L, "SH_2")));

        final var result = searchHelpService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateSearchHelp_whenValid() {
        final var entity = createSearchHelp(1L, "OLD_SH");
        final var newTable = createTableDefinition(2L);
        final var request = new CreateSearchHelpRequest("NEW_SH", 2L, "Updated");
        when(searchHelpRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(searchHelpRepository.findBySearchHelpName("NEW_SH")).thenReturn(Optional.empty());
        when(tableDefinitionRepository.findById(2L)).thenReturn(Optional.of(newTable));
        when(searchHelpRepository.save(any(SearchHelp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = searchHelpService.update(1L, request);

        assertThat(result.searchHelpName()).isEqualTo("NEW_SH");
    }

    @Test
    void shouldUpdateSearchHelp_whenNameUnchanged() {
        final var entity = createSearchHelp(1L, "SAME_SH");
        final var table = createTableDefinition(1L);
        final var request = new CreateSearchHelpRequest("SAME_SH", 1L, "Updated");
        when(searchHelpRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(searchHelpRepository.findBySearchHelpName("SAME_SH")).thenReturn(Optional.of(entity));
        when(tableDefinitionRepository.findById(1L)).thenReturn(Optional.of(table));
        when(searchHelpRepository.save(any(SearchHelp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = searchHelpService.update(1L, request);

        assertThat(result.searchHelpName()).isEqualTo("SAME_SH");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdatingNonExistentSearchHelp() {
        final var request = new CreateSearchHelpRequest("SH", 1L, null);
        when(searchHelpRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> searchHelpService.update(99L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowDuplicateEntityException_whenUpdateSearchHelpNameConflicts() {
        final var entity = createSearchHelp(1L, "ORIGINAL");
        final var conflicting = createSearchHelp(2L, "TAKEN");
        final var request = new CreateSearchHelpRequest("TAKEN", 1L, null);
        when(searchHelpRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(searchHelpRepository.findBySearchHelpName("TAKEN")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> searchHelpService.update(1L, request))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdateTableNotFound() {
        final var entity = createSearchHelp(1L, "SH");
        final var request = new CreateSearchHelpRequest("NEW_SH", 99L, null);
        when(searchHelpRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(searchHelpRepository.findBySearchHelpName("NEW_SH")).thenReturn(Optional.empty());
        when(tableDefinitionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> searchHelpService.update(1L, request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDeleteSearchHelp_whenIdExists() {
        when(searchHelpRepository.existsById(1L)).thenReturn(true);

        searchHelpService.delete(1L);

        verify(searchHelpRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentSearchHelp() {
        when(searchHelpRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> searchHelpService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private TableDefinition createTableDefinition(final Long id) {
        final var table = new TableDefinition();
        table.setId(id);
        table.setTableName("TABLE_" + id);
        table.setCreatedAt(Instant.now());
        table.setUpdatedAt(Instant.now());
        return table;
    }

    private SearchHelp createSearchHelp(final Long id, final String name) {
        final var table = createTableDefinition(1L);
        final var searchHelp = new SearchHelp();
        searchHelp.setId(id);
        searchHelp.setSearchHelpName(name);
        searchHelp.setTableDefinition(table);
        searchHelp.setDescription("Test search help");
        searchHelp.setCreatedAt(Instant.now());
        searchHelp.setUpdatedAt(Instant.now());
        return searchHelp;
    }
}
