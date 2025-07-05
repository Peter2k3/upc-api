package upc.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import upc.api.model.Permission;
import upc.api.model.Role;
import upc.api.model.RolePermission;
import upc.api.repository.PermissionRepository;
import upc.api.repository.RolePermissionRepository;
import upc.api.repository.RoleRepository;
import upc.api.service.impl.RoleServiceImpl;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private Permission permission;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        permission = Permission.builder()
                .id(1L)
                .name("READ_USERS")
                .description("Read users permission")
                .resource("users")
                .action("READ")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void saveRole_ShouldReturnSavedRole() {
        // Given
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // When
        Role savedRole = roleService.saveRole(role);

        // Then
        assertNotNull(savedRole);
        assertEquals("ADMIN", savedRole.getName());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void getAllActiveRoles_ShouldReturnListOfActiveRoles() {
        // Given
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAllActiveRoles()).thenReturn(roles);

        // When
        List<Role> result = roleService.getAllActiveRoles();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ADMIN", result.get(0).getName());
        verify(roleRepository).findAllActiveRoles();
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenExists() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // When
        Optional<Role> result = roleService.getRoleById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
        verify(roleRepository).findById(1L);
    }

    @Test
    void getRoleById_ShouldReturnEmpty_WhenNotExists() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Role> result = roleService.getRoleById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(roleRepository).findById(1L);
    }

    @Test
    void existsByNombre_ShouldReturnTrue_WhenExists() {
        // Given
        when(roleRepository.existsByName("ADMIN")).thenReturn(true);

        // When
        boolean exists = roleService.existsByNombre("ADMIN");

        // Then
        assertTrue(exists);
        verify(roleRepository).existsByName("ADMIN");
    }

    @Test
    void assignPermissionToRole_ShouldReturnTrue_WhenSuccessful() {
        // Given
        when(rolePermissionRepository.existsByRol_IdAndPermiso_Id(1L, 1L)).thenReturn(false);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(new RolePermission());

        // When
        boolean result = roleService.assignPermissionToRole(1L, 1L);

        // Then
        assertTrue(result);
        verify(rolePermissionRepository).save(any(RolePermission.class));
    }

    @Test
    void assignPermissionToRole_ShouldReturnFalse_WhenAlreadyExists() {
        // Given
        when(rolePermissionRepository.existsByRol_IdAndPermiso_Id(1L, 1L)).thenReturn(true);

        // When
        boolean result = roleService.assignPermissionToRole(1L, 1L);

        // Then
        assertFalse(result);
        verify(rolePermissionRepository, never()).save(any(RolePermission.class));
    }

    @Test
    void softDeleteRole_ShouldDeactivateRole() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // When
        roleService.softDeleteRole(1L);

        // Then
        assertFalse(role.getActive());
        verify(roleRepository).save(role);
    }
}
