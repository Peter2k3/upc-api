package upc.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upc.api.model.Aviso;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvisosRepository extends JpaRepository<Aviso, Integer>{

    Optional<Aviso> findByFijado(boolean fijado);

    @Query("SELECT a FROM avisos a WHERE a.fechaInicio <= :fecha " +
        "AND a.fechaFinalizacion > :fecha AND a.status = true")
    List<Aviso> buscarAvisosActivos(@Param("fecha") LocalDateTime fecha);

}
