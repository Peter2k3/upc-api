package upc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upc.api.model.Permission;
import upc.api.service.IPermissionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PERMISSIONS')")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllActivePermissions();
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PERMISSIONS')")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Optional<Permission> permission = permissionService.getPermissionById(id);
        return permission.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                         .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PERMISSIONS')")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        try {
            if (permissionService.existsByNombre(permission.getName())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Permission savedPermission = permissionService.savePermission(permission);
            return new ResponseEntity<>(savedPermission, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PERMISSIONS')")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        Optional<Permission> existingPermission = permissionService.getPermissionById(id);
        if (existingPermission.isPresent()) {
            permission.setId(id);
            Permission updatedPermission = permissionService.updatePermission(permission);
            return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PERMISSIONS')")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        Optional<Permission> permission = permissionService.getPermissionById(id);
        if (permission.isPresent()) {
            permissionService.deletePermission(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
