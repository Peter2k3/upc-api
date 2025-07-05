package upc.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upc.api.model.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Role> findByActiveTrue();
    
    @Query("SELECT r FROM Role r WHERE r.active = true ORDER BY r.name ASC")
    List<Role> findAllActiveRoles();
    
    @Query("SELECT r FROM Role r JOIN r.rolePermissions rp WHERE rp.permiso.id = :permissionId AND r.active = true")
    List<Role> findRolesByPermissionId(@Param("permissionId") Long permissionId);
}
