package upc.api.service;

import upc.api.model.Role;
import upc.api.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    
    Role saveRole(Role role);
    
    List<Role> getAllRoles();
    
    List<Role> getAllActiveRoles();
    
    Optional<Role> getRoleById(Long id);
    
    Optional<Role> getRoleByNombre(String nombre);
    
    Role updateRole(Role role);
    
    void deleteRole(Long id);
    
    void softDeleteRole(Long id);
    
    boolean existsByNombre(String nombre);
    
    List<Role> getRolesByPermission(Long permissionId);
    
    boolean assignPermissionToRole(Long roleId, Long permissionId);
    
    boolean removePermissionFromRole(Long roleId, Long permissionId);
    
    List<Permission> getPermissionsByRole(Long roleId);
}
