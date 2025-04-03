package upc.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upc.api.model.Notice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvisosRepository extends JpaRepository<Notice, Integer> {

    Optional<Notice> findByFijado(boolean fijado);

    @Query("SELECT n FROM notices n WHERE n.startDate <= :fecha " +
        "AND n.endDate > :fecha AND n.status = true")
    List<Notice> buscarAvisosActivos(@Param("fecha") LocalDateTime fecha);

}
