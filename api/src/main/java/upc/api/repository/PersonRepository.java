package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByDni(String dni);

    boolean existsByDni(String dni);

    Optional<Person> findByTelefono(String telefono);

    List<Person> findByNombresContainingOrPaternoContaining(String nombres, String paterno);

    List<Person> findByActivoTrue();

    @Query("SELECT p FROM Person p WHERE p.activo = true ORDER BY p.paterno ASC, p.nombres ASC")
    List<Person> findAllActivePersonsOrderedByName();

    @Query("SELECT p FROM Person p JOIN PersonPosition pp ON pp.persona.id = p.id WHERE pp.puesto.id = :positionId AND p.activo = true")
    List<Person> findByPositionId(@Param("positionId") Long positionId);

    @Query("SELECT p FROM Person p JOIN UserPerson up ON up.persona.id = p.id WHERE up.usuario.id = :userId")
    Optional<Person> findByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Person p WHERE LOWER(p.nombres) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.paterno) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR p.dni LIKE CONCAT('%', :searchTerm, '%')")
    List<Person> searchPersons(@Param("searchTerm") String searchTerm);
}
