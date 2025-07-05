package upc.api.service;

import java.util.List;
import java.util.Optional;

import upc.api.model.User;

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

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
