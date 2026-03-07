package com.erp.kernel.security.controller;

import com.erp.kernel.security.dto.CreateUserRequest;
import com.erp.kernel.security.dto.UserDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link UserController}.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldCreateUser_whenValidRequest() throws Exception {
        final var request = new CreateUserRequest("jdoe", "Pass123!", "jdoe@erp.com",
                "John", "Doe", "John Doe", null);
        final var now = Instant.now();
        final var response = new UserDto(1L, "jdoe", "jdoe@erp.com", "John", "Doe",
                "John Doe", true, false, null, null, null, 0, now, now);
        when(userService.create(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/security/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("jdoe"));
    }

    @Test
    void shouldReturnUser_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new UserDto(1L, "jdoe", "jdoe@erp.com", "John", "Doe",
                null, true, false, null, null, null, 0, now, now);
        when(userService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/security/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        final var now = Instant.now();
        when(userService.findAll()).thenReturn(List.of(
                new UserDto(1L, "u1", "u1@e.com", "F", "L", null, true, false, null, null, null, 0, now, now)));

        mockMvc.perform(get("/api/v1/security/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        final var request = new CreateUserRequest("updated", null, "updated@erp.com",
                "Up", "Dated", null, null);
        final var now = Instant.now();
        final var response = new UserDto(1L, "updated", "updated@erp.com", "Up", "Dated",
                null, true, false, null, null, null, 0, now, now);
        when(userService.update(eq(1L), any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/security/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/security/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).delete(1L);
    }

    @Test
    void shouldReturnConflict_whenUsernameExists() throws Exception {
        final var request = new CreateUserRequest("jdoe", "Pass123!", "jdoe@erp.com",
                "John", "Doe", null, null);
        when(userService.create(any())).thenThrow(new DuplicateEntityException("exists"));

        mockMvc.perform(post("/api/v1/security/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFound_whenUserNotFound() throws Exception {
        when(userService.findById(99L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/security/users/99"))
                .andExpect(status().isNotFound());
    }
}
