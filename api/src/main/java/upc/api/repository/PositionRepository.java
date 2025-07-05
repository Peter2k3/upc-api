package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Position> findByActiveTrue();

    @Query("SELECT pos FROM Position pos WHERE pos.active = true ORDER BY pos.nombre ASC")
    List<Position> findAllActivePositions();

    @Query("SELECT pos FROM Position pos JOIN pos.personPositions pp WHERE pp.persona.id = :personId AND pos.active = true")
    List<Position> findPositionsByPersonId(@Param("personId") Long personId);

    @Query("SELECT COUNT(pp) FROM PersonPosition pp WHERE pp.puesto.id = :positionId")
    Long countPersonsByPositionId(@Param("positionId") Long positionId);
}
