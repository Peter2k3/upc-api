package upc.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import upc.api.model.Role;
import upc.api.service.IRoleService;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser(authorities = { "READ_ROLES" })
    void getAllRoles_ShouldReturnRolesList() throws Exception {
        // Given
        List<Role> roles = Arrays.asList(role);
        when(roleService.getAllActiveRoles()).thenReturn(roles);

        // When & Then
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("ADMIN")))
                .andExpect(jsonPath("$[0].description", is("Administrator role")));

        verify(roleService).getAllActiveRoles();
    }

    @Test
    @WithMockUser(authorities = { "READ_ROLES" })
    void getRoleById_ShouldReturnRole_WhenExists() throws Exception {
        // Given
        when(roleService.getRoleById(1L)).thenReturn(Optional.of(role));

        // When & Then
        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("ADMIN")))
                .andExpect(jsonPath("$.description", is("Administrator role")));

        verify(roleService).getRoleById(1L);
    }

    @Test
    @WithMockUser(authorities = { "READ_ROLES" })
    void getRoleById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        // Given
        when(roleService.getRoleById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isNotFound());

        verify(roleService).getRoleById(1L);
    }

    @Test
    @WithMockUser(authorities = { "CREATE_ROLES" })
    void createRole_ShouldReturnCreatedRole() throws Exception {
        // Given
        when(roleService.existsByNombre("ADMIN")).thenReturn(false);
        when(roleService.saveRole(any(Role.class))).thenReturn(role);

        // When & Then
        mockMvc.perform(post("/api/roles")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("ADMIN")))
                .andExpect(jsonPath("$.description", is("Administrator role")));

        verify(roleService).existsByNombre("ADMIN");
        verify(roleService).saveRole(any(Role.class));
    }

    @Test
    @WithMockUser(authorities = { "CREATE_ROLES" })
    void createRole_ShouldReturnConflict_WhenRoleExists() throws Exception {
        // Given
        when(roleService.existsByNombre("ADMIN")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/roles")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isConflict());

        verify(roleService).existsByNombre("ADMIN");
        verify(roleService, never()).saveRole(any(Role.class));
    }

    @Test
    @WithMockUser(authorities = { "UPDATE_ROLES" })
    void updateRole_ShouldReturnUpdatedRole() throws Exception {
        // Given
        when(roleService.getRoleById(1L)).thenReturn(Optional.of(role));
        when(roleService.updateRole(any(Role.class))).thenReturn(role);

        // When & Then
        mockMvc.perform(put("/api/roles/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("ADMIN")));

        verify(roleService).getRoleById(1L);
        verify(roleService).updateRole(any(Role.class));
    }

    @Test
    @WithMockUser(authorities = { "UPDATE_ROLES" })
    void updateRole_ShouldReturnNotFound_WhenRoleNotExists() throws Exception {
        // Given
        when(roleService.getRoleById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/roles/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isNotFound());

        verify(roleService).getRoleById(1L);
        verify(roleService, never()).updateRole(any(Role.class));
    }

    @Test
    @WithMockUser(authorities = { "DELETE_ROLES" })
    void deleteRole_ShouldReturnNoContent() throws Exception {
        // Given
        when(roleService.getRoleById(1L)).thenReturn(Optional.of(role));

        // When & Then
        mockMvc.perform(delete("/api/roles/1")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(roleService).getRoleById(1L);
        verify(roleService).softDeleteRole(1L);
    }

    @Test
    @WithMockUser(authorities = { "DELETE_ROLES" })
    void deleteRole_ShouldReturnNotFound_WhenRoleNotExists() throws Exception {
        // Given
        when(roleService.getRoleById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/roles/1")
                .with(csrf()))
                .andExpect(status().isNotFound());

        verify(roleService).getRoleById(1L);
        verify(roleService, never()).softDeleteRole(anyLong());
    }

    @Test
    @WithMockUser(authorities = { "ASSIGN_PERMISSIONS" })
    void assignPermissionToRole_ShouldReturnOk() throws Exception {
        // Given
        when(roleService.assignPermissionToRole(1L, 1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/roles/1/permissions/1")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(roleService).assignPermissionToRole(1L, 1L);
    }

    @Test
    @WithMockUser(authorities = { "ASSIGN_PERMISSIONS" })
    void assignPermissionToRole_ShouldReturnBadRequest_WhenFails() throws Exception {
        // Given
        when(roleService.assignPermissionToRole(1L, 1L)).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/roles/1/permissions/1")
                .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(roleService).assignPermissionToRole(1L, 1L);
    }

    @Test
    @WithMockUser
    void getAllRoles_ShouldReturnForbidden_WhenNoPermission() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isForbidden());

        verify(roleService, never()).getAllActiveRoles();
    }
}
