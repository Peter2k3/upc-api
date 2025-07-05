package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.RolePermission;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRol_Id(Long roleId);

    List<RolePermission> findByPermiso_Id(Long permissionId);

    Optional<RolePermission> findByRol_IdAndPermiso_Id(Long roleId, Long permissionId);

    boolean existsByRol_IdAndPermiso_Id(Long roleId, Long permissionId);

    @Query("SELECT rp FROM RolePermission rp WHERE rp.rol.id = :roleId AND rp.permiso.active = true")
    List<RolePermission> findActivePermissionsByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT rp FROM RolePermission rp WHERE rp.permiso.id = :permissionId AND rp.rol.active = true")
    List<RolePermission> findActiveRolesByPermissionId(@Param("permissionId") Long permissionId);
}
