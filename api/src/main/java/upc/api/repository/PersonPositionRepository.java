package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.PersonPosition;

@Repository
public interface PersonPositionRepository extends JpaRepository<PersonPosition, Long> {

    Optional<PersonPosition> findByPersona_Id(Long personId);

    List<PersonPosition> findByPuesto_Id(Long positionId);

    Optional<PersonPosition> findByPersona_IdAndPuesto_Id(Long personId, Long positionId);

    boolean existsByPersona_Id(Long personId);

    boolean existsByPersona_IdAndPuesto_Id(Long personId, Long positionId);

    @Query("SELECT pp FROM PersonPosition pp WHERE pp.persona.activo = true AND pp.puesto.active = true")
    List<PersonPosition> findAllActiveRelations();

    @Query("SELECT pp FROM PersonPosition pp WHERE pp.puesto.id = :positionId AND pp.persona.activo = true")
    List<PersonPosition> findActivePersonsByPositionId(@Param("positionId") Long positionId);
}
