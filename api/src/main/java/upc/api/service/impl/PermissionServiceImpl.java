package upc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.api.model.Permission;
import upc.api.model.Role;
import upc.api.repository.PermissionRepository;
import upc.api.repository.RolePermissionRepository;
import upc.api.service.IPermissionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public Permission savePermission(Permission permission) {
        if (permission.getCreatedAt() == null) {
            permission.setCreatedAt(LocalDateTime.now());
        }
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> getAllActivePermissions() {
        return permissionRepository.findAll(); // No hay campo activo en Permission
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> getPermissionByNombre(String nombre) {
        return permissionRepository.findByName(nombre);
    }

    @Override
    public Permission updatePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public void softDeletePermission(Long id) {
        // No implementado - Permission no tiene campo activo
        deletePermission(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return permissionRepository.existsByName(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> getPermissionsByRole(Long roleId) {
        return permissionRepository.findPermissionsByRoleId(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesByPermission(Long permissionId) {
        return rolePermissionRepository.findActiveRolesByPermissionId(permissionId)
                .stream()
                .map(rp -> rp.getRol())
                .toList();
    }
}
