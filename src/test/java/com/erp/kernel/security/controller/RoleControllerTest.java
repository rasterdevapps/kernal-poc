package com.erp.kernel.security.controller;

import com.erp.kernel.security.dto.CreateRoleRequest;
import com.erp.kernel.security.dto.RoleDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.service.RoleService;
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
 * Tests for the {@link RoleController}.
 */
@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleService roleService;

    @Test
    void shouldCreateRole_whenValidRequest() throws Exception {
        final var request = new CreateRoleRequest("ADMIN", "Administrator");
        final var now = Instant.now();
        final var response = new RoleDto(1L, "ADMIN", "Administrator", true, now, now);
        when(roleService.create(any(CreateRoleRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/security/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("ADMIN"));
    }

    @Test
    void shouldReturnRole_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new RoleDto(1L, "ADMIN", "Administrator", true, now, now);
        when(roleService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/security/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnAllRoles() throws Exception {
        final var now = Instant.now();
        when(roleService.findAll()).thenReturn(List.of(
                new RoleDto(1L, "ADMIN", "Admin", true, now, now)));

        mockMvc.perform(get("/api/v1/security/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateRole() throws Exception {
        final var request = new CreateRoleRequest("UPDATED", "Updated role");
        final var now = Instant.now();
        final var response = new RoleDto(1L, "UPDATED", "Updated role", true, now, now);
        when(roleService.update(eq(1L), any(CreateRoleRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/security/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("UPDATED"));
    }

    @Test
    void shouldDeleteRole() throws Exception {
        mockMvc.perform(delete("/api/v1/security/roles/1"))
                .andExpect(status().isNoContent());

        verify(roleService).delete(1L);
    }

    @Test
    void shouldReturnConflict_whenRoleNameExists() throws Exception {
        final var request = new CreateRoleRequest("ADMIN", "Administrator");
        when(roleService.create(any())).thenThrow(new DuplicateEntityException("exists"));

        mockMvc.perform(post("/api/v1/security/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFound_whenRoleNotFound() throws Exception {
        when(roleService.findById(99L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/security/roles/99"))
                .andExpect(status().isNotFound());
    }
}
