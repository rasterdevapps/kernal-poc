package com.erp.kernel.numberrange.controller;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.exception.GlobalExceptionHandler;
import com.erp.kernel.numberrange.dto.CreateIntervalRequest;
import com.erp.kernel.numberrange.dto.CreateNumberRangeRequest;
import com.erp.kernel.numberrange.dto.NextNumberResponse;
import com.erp.kernel.numberrange.dto.NumberRangeDto;
import com.erp.kernel.numberrange.dto.NumberRangeIntervalDto;
import com.erp.kernel.numberrange.service.NumberRangeService;
import com.erp.kernel.api.jwt.JwtTokenService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.erp.kernel.api.config.CorsProperties;

/**
 * Tests for {@link NumberRangeController}.
 */
@WebMvcTest(NumberRangeController.class)
@AutoConfigureMockMvc(addFilters = false)
class NumberRangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NumberRangeService numberRangeService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    private static final String BASE_URL = "/api/v1/number-ranges";

    @Test
    void shouldCreateNumberRange() throws Exception {
        final var request = new CreateNumberRangeRequest("ORDER_NR", "Orders", true, 100);
        final var now = Instant.now();
        final var dto = new NumberRangeDto(1L, "ORDER_NR", "Orders", true, 100, now, now);
        when(numberRangeService.create(any())).thenReturn(dto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rangeObject").value("ORDER_NR"));
    }

    @Test
    void shouldReturn409_whenDuplicateRange() throws Exception {
        final var request = new CreateNumberRangeRequest("ORDER_NR", "Orders", true, 100);
        when(numberRangeService.create(any()))
                .thenThrow(new DuplicateEntityException("exists"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNumberRange_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var dto = new NumberRangeDto(1L, "ORDER_NR", "Orders", true, 100, now, now);
        when(numberRangeService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rangeObject").value("ORDER_NR"));
    }

    @Test
    void shouldReturn404_whenRangeNotFound() throws Exception {
        when(numberRangeService.findById(99L))
                .thenThrow(new EntityNotFoundException("not found"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllRanges() throws Exception {
        final var now = Instant.now();
        when(numberRangeService.findAll()).thenReturn(List.of(
                new NumberRangeDto(1L, "ORDER_NR", "Orders", true, 100, now, now)));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateNumberRange() throws Exception {
        final var request = new CreateNumberRangeRequest("UPDATED", "Updated", false, null);
        final var now = Instant.now();
        final var dto = new NumberRangeDto(1L, "UPDATED", "Updated", false, null, now, now);
        when(numberRangeService.update(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rangeObject").value("UPDATED"));
    }

    @Test
    void shouldDeleteNumberRange() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404_whenDeletingNonExistentRange() throws Exception {
        doThrow(new EntityNotFoundException("not found"))
                .when(numberRangeService).delete(99L);

        mockMvc.perform(delete(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateInterval() throws Exception {
        final var request = new CreateIntervalRequest("01", "1", "999");
        final var now = Instant.now();
        final var dto = new NumberRangeIntervalDto(10L, 1L, "01", "1", "999", "1", now, now);
        when(numberRangeService.createInterval(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(post(BASE_URL + "/1/intervals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subObject").value("01"));
    }

    @Test
    void shouldReturnIntervals() throws Exception {
        final var now = Instant.now();
        when(numberRangeService.findIntervals(1L)).thenReturn(List.of(
                new NumberRangeIntervalDto(10L, 1L, "01", "1", "999", "50", now, now)));

        mockMvc.perform(get(BASE_URL + "/1/intervals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldAssignNextNumber() throws Exception {
        final var response = new NextNumberResponse("ORDER_NR", "01", "51");
        when(numberRangeService.getNextNumber("ORDER_NR", "01")).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/ORDER_NR/next/01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedNumber").value("51"));
    }
}
