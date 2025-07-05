package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUsuario_Id(Long userId);

    List<UserRole> findByRol_Id(Long roleId);

    Optional<UserRole> findByUsuario_IdAndRol_Id(Long userId, Long roleId);

    boolean existsByUsuario_IdAndRol_Id(Long userId, Long roleId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.usuario.id = :userId AND ur.rol.active = true")
    List<UserRole> findActiveRolesByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.rol.id = :roleId AND ur.usuario.active = true")
    List<UserRole> findActiveUsersByRoleId(@Param("roleId") Long roleId);
}
