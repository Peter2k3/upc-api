package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByActive(Boolean active);

    boolean existsByEmail(String email);

    List<User> findByNombresContainingOrPaternoContaining(String nombres, String paterno);

    Optional<User> findByIdAndActive(Long id, Boolean active);

    // Actualizado para el nuevo modelo donde User tiene un solo Role
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName")
    List<User> findByRoles_Name(@Param("roleName") String roleName);

    // Nuevos métodos para el modelo actualizado
    @Query("SELECT u FROM User u WHERE u.role.id = :roleId AND u.active = true")
    List<User> findActiveUsersByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT u FROM User u WHERE u.active = true ORDER BY u.paterno ASC, u.nombres ASC")
    List<User> findAllActiveUsersOrderedByName();
}
