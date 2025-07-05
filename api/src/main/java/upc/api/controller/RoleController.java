package upc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upc.api.model.Role;
import upc.api.model.Permission;
import upc.api.service.IRoleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ROLES')")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllActiveRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_ROLES')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.getRoleById(id);
        return role.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLES')")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        try {
            if (roleService.existsByNombre(role.getName())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Role savedRole = roleService.saveRole(role);
            return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ROLES')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        Optional<Role> existingRole = roleService.getRoleById(id);
        if (existingRole.isPresent()) {
            role.setId(id);
            Role updatedRole = roleService.updateRole(role);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLES')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            roleService.softDeleteRole(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ASSIGN_PERMISSIONS')")
    public ResponseEntity<Void> assignPermissionToRole(
            @PathVariable Long roleId, 
            @PathVariable Long permissionId) {
        boolean assigned = roleService.assignPermissionToRole(roleId, permissionId);
        return assigned ? new ResponseEntity<>(HttpStatus.OK) 
                       : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('REMOVE_PERMISSIONS')")
    public ResponseEntity<Void> removePermissionFromRole(
            @PathVariable Long roleId, 
            @PathVariable Long permissionId) {
        boolean removed = roleService.removePermissionFromRole(roleId, permissionId);
        return removed ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
                       : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('READ_PERMISSIONS')")
    public ResponseEntity<List<Permission>> getRolePermissions(@PathVariable Long id) {
        List<Permission> permissions = roleService.getPermissionsByRole(id);
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }
}
