package upc.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import upc.api.model.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .name("ADMIN")
                .description("Administrator role")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void save_ShouldPersistRole() {
        // When
        Role savedRole = roleRepository.save(role);

        // Then
        assertNotNull(savedRole);
        assertNotNull(savedRole.getId());
        assertEquals("ADMIN", savedRole.getName());
        assertEquals("Administrator role", savedRole.getDescription());
        assertTrue(savedRole.getActive());
    }

    @Test
    void findByName_ShouldReturnRole_WhenExists() {
        // Given
        roleRepository.save(role);

        // When
        Optional<Role> foundRole = roleRepository.findByName("ADMIN");

        // Then
        assertTrue(foundRole.isPresent());
        assertEquals("ADMIN", foundRole.get().getName());
        assertEquals("Administrator role", foundRole.get().getDescription());
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNotExists() {
        // When
        Optional<Role> foundRole = roleRepository.findByName("NON_EXISTENT");

        // Then
        assertFalse(foundRole.isPresent());
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenExists() {
        // Given
        roleRepository.save(role);

        // When
        boolean exists = roleRepository.existsByName("ADMIN");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNotExists() {
        // When
        boolean exists = roleRepository.existsByName("NON_EXISTENT");

        // Then
        assertFalse(exists);
    }

    @Test
    void findAllActiveRoles_ShouldReturnOnlyActiveRoles() {
        // Given
        Role activeRole = Role.builder()
                .name("ACTIVE_ROLE")
                .description("Active role")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Role inactiveRole = Role.builder()
                .name("INACTIVE_ROLE")
                .description("Inactive role")
                .active(false)
                .createdAt(LocalDateTime.now())
                .build();

        roleRepository.save(activeRole);
        roleRepository.save(inactiveRole);

        // When
        List<Role> activeRoles = roleRepository.findAllActiveRoles();

        // Then
        assertNotNull(activeRoles);
        assertEquals(1, activeRoles.size());
        assertEquals("ACTIVE_ROLE", activeRoles.get(0).getName());
        assertTrue(activeRoles.get(0).getActive());
    }

    @Test
    void findByActiveTrue_ShouldReturnOnlyActiveRoles() {
        // Given
        Role activeRole1 = Role.builder()
                .name("ACTIVE_ROLE_1")
                .description("Active role 1")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Role activeRole2 = Role.builder()
                .name("ACTIVE_ROLE_2")
                .description("Active role 2")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Role inactiveRole = Role.builder()
                .name("INACTIVE_ROLE")
                .description("Inactive role")
                .active(false)
                .createdAt(LocalDateTime.now())
                .build();

        roleRepository.save(activeRole1);
        roleRepository.save(activeRole2);
        roleRepository.save(inactiveRole);

        // When
        List<Role> activeRoles = roleRepository.findByActiveTrue();

        // Then
        assertNotNull(activeRoles);
        assertEquals(2, activeRoles.size());
        activeRoles.forEach(r -> assertTrue(r.getActive()));
    }

    @Test
    void deleteById_ShouldRemoveRole() {
        // Given
        Role savedRole = roleRepository.save(role);
        Long roleId = savedRole.getId();

        // When
        roleRepository.deleteById(roleId);

        // Then
        Optional<Role> deletedRole = roleRepository.findById(roleId);
        assertFalse(deletedRole.isPresent());
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        // Given
        roleRepository.save(role);
        Role secondRole = Role.builder()
                .name("USER")
                .description("User role")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        roleRepository.save(secondRole);

        // When
        long count = roleRepository.count();

        // Then
        assertEquals(2, count);
    }
}
