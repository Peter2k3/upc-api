package upc.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upc.api.model.Permission;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT p FROM Permission p ORDER BY p.name ASC")
    List<Permission> findAllActivePermissions();
    
    @Query("SELECT p FROM Permission p JOIN p.rolePermissions rp WHERE rp.rol.id = :roleId")
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);
}
