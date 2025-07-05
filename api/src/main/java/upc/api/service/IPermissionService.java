package upc.api.service;

import upc.api.model.Permission;
import upc.api.model.Role;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    
    Permission savePermission(Permission permission);
    
    List<Permission> getAllPermissions();
    
    List<Permission> getAllActivePermissions();
    
    Optional<Permission> getPermissionById(Long id);
    
    Optional<Permission> getPermissionByNombre(String nombre);
    
    Permission updatePermission(Permission permission);
    
    void deletePermission(Long id);
    
    void softDeletePermission(Long id);
    
    boolean existsByNombre(String nombre);
    
    List<Permission> getPermissionsByRole(Long roleId);
    
    List<Role> getRolesByPermission(Long permissionId);
}
