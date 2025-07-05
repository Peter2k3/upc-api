package upc.api.repository;

import mx.ccgsgroup.ccgs_backend_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByActive(Boolean active);

    boolean existsByEmail(String email);

    List<User> findByNameContainingOrLastnameContaining(String name, String lastname);

    Optional<User> findByIdUserAndActive(Long idUser, Boolean active);

    List<User> findByRoles_Name(String roleName);
}
