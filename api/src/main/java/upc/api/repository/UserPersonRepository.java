package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upc.api.model.UserPerson;

@Repository
public interface UserPersonRepository extends JpaRepository<UserPerson, Long> {

    Optional<UserPerson> findByUsuario_Id(Long userId);

    Optional<UserPerson> findByPersona_Id(Long personId);

    Optional<UserPerson> findByUsuario_IdAndPersona_Id(Long userId, Long personId);

    boolean existsByUsuario_Id(Long userId);

    boolean existsByPersona_Id(Long personId);

    boolean existsByUsuario_IdAndPersona_Id(Long userId, Long personId);

    @Query("SELECT up FROM UserPerson up WHERE up.usuario.active = true AND up.persona.activo = true")
    List<UserPerson> findAllActiveRelations();
}
