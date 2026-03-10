package com.erp.kernel.navigation.controller;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateFavouriteRequest;
import com.erp.kernel.navigation.dto.FavouriteDto;
import com.erp.kernel.navigation.service.FavouriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link FavouriteController}.
 */
@WebMvcTest(FavouriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavouriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FavouriteService favouriteService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateFavourite_whenValidRequest() throws Exception {
        final var request = new CreateFavouriteRequest(1L, 2L, 0);
        final var now = Instant.now();
        final var response = new FavouriteDto(1L, 1L, 2L, 0, now, now);
        when(favouriteService.create(any(CreateFavouriteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/navigation/favourites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void shouldReturnFavourite_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new FavouriteDto(1L, 1L, 2L, 0, now, now);
        when(favouriteService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/favourites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnFavouritesForUser() throws Exception {
        final var now = Instant.now();
        when(favouriteService.findByUserId(1L)).thenReturn(List.of(
                new FavouriteDto(1L, 1L, 2L, 0, now, now)));

        mockMvc.perform(get("/api/v1/navigation/favourites/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldDeleteFavourite() throws Exception {
        mockMvc.perform(delete("/api/v1/navigation/favourites/1"))
                .andExpect(status().isNoContent());

        verify(favouriteService).delete(1L);
    }

    @Test
    void shouldReturnNotFound_whenFavouriteNotFound() throws Exception {
        when(favouriteService.findById(99L)).thenThrow(new EntityNotFoundException("Favourite not found"));

        mockMvc.perform(get("/api/v1/navigation/favourites/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflict_whenFavouriteExists() throws Exception {
        final var request = new CreateFavouriteRequest(1L, 2L, 0);
        when(favouriteService.create(any())).thenThrow(new DuplicateEntityException("Favourite already exists"));

        mockMvc.perform(post("/api/v1/navigation/favourites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
