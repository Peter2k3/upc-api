package upc.api.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import upc.api.model.Permission;
import upc.api.model.Role;
import upc.api.model.RolePermission;
import upc.api.repository.PermissionRepository;
import upc.api.repository.RolePermissionRepository;
import upc.api.repository.RoleRepository;
import upc.api.service.IRoleService;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public Role saveRole(Role role) {
        if (role.getCreatedAt() == null) {
            role.setCreatedAt(LocalDateTime.now());
        }
        if (role.getActive() == null) {
            role.setActive(true);
        }
        return roleRepository.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllActiveRoles() {
        return roleRepository.findAllActiveRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleByNombre(String nombre) {
        return roleRepository.findByName(nombre);
    }

    @Override
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void softDeleteRole(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            Role r = role.get();
            r.setActive(false);
            roleRepository.save(r);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return roleRepository.existsByName(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesByPermission(Long permissionId) {
        return roleRepository.findRolesByPermissionId(permissionId);
    }

    @Override
    public boolean assignPermissionToRole(Long roleId, Long permissionId) {
        if (rolePermissionRepository.existsByRol_IdAndPermiso_Id(roleId, permissionId)) {
            return false; // Ya existe la relación
        }

        Optional<Role> roleOpt = roleRepository.findById(roleId);
        Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);

        if (roleOpt.isPresent() && permissionOpt.isPresent()) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRol(roleOpt.get());
            rolePermission.setPermiso(permissionOpt.get());
            rolePermission.setAssignedAt(LocalDateTime.now());
            rolePermissionRepository.save(rolePermission);
            return true;
        }
        return false;
    }

    @Override
    public boolean removePermissionFromRole(Long roleId, Long permissionId) {
        Optional<RolePermission> rolePermission = rolePermissionRepository
                .findByRol_IdAndPermiso_Id(roleId, permissionId);
        if (rolePermission.isPresent()) {
            rolePermissionRepository.delete(rolePermission.get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> getPermissionsByRole(Long roleId) {
        return rolePermissionRepository.findActivePermissionsByRoleId(roleId)
                .stream()
                .map(RolePermission::getPermiso)
                .collect(Collectors.toList());
    }
}
