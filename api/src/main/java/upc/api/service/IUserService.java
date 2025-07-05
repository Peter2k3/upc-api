package upc.api.service;

import mx.ccgsgroup.ccgs_backend_api.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User saveUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    User updateUser(User user);

    void deleteUser(Long id);

    void softDeleteUser(Long id);

    boolean isEmailTaken(String email);

    List<User> searchUsersByNameOrLastname(String searchTerm);

    List<User> getUsersByRole(String roleName);

    boolean activateUser(Long id);

    void updateLastAccess(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
