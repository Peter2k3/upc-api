package upc.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.api.model.User;
import upc.api.service.IUserService;

import java.util.List;
import java.util.Optional;

//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
public class UserController {
/*
//    private final IUserService userService;
//
//    /**
//     * Obtiene todos los usuarios
//     * @return Lista de usuarios
//     */
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//
//    /**
//     * Obtiene un usuario por su ID
//     * @param id ID del usuario
//     * @return Usuario encontrado o 404 si no existe
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Long id) {
//        Optional<User> user = userService.getUserById(id);
//        return user.map(ResponseEntity::ok)
//            .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * Actualiza un usuario existente
//     * @param id ID del usuario a actualizar
//     * @param user Datos del usuario actualizado
//     * @return Usuario actualizado o 404 si no existe
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
//        if (userService.getUserById(id).isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        user.setIdUser(id); // Asegura que el ID sea el correcto
//        User updatedUser = userService.updateUser(user);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    /**
//     * Elimina un usuario por su ID
//     * @param id ID del usuario a eliminar
//     * @return 204 No Content si se eliminó correctamente, 404 si no existe
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        if (userService.getUserById(id).isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
}
