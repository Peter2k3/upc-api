package upc.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.Notice;
import upc.api.model.enums.PrioridadAviso;
import upc.api.model.enums.TipoAviso;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByTipo(TipoAviso tipo);

    List<Notice> findByPrioridad(PrioridadAviso prioridad);

    List<Notice> findByActivoTrue();

    Optional<Notice> findByFijado(boolean fijado);

    @Query("SELECT n FROM Notice n WHERE n.fechaInicio <= :fecha " +
            "AND n.fechaFin >= :fecha AND n.activo = true")
    List<Notice> findActiveNotices(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT n FROM Notice n WHERE n.fechaInicio <= :now " +
            "AND n.fechaFin >= :now AND n.activo = true ORDER BY n.prioridad DESC, n.createdAt DESC")
    List<Notice> findCurrentActiveNoticesOrderedByPriority(@Param("now") LocalDateTime now);

    @Query("SELECT n FROM Notice n WHERE n.fijado = true AND n.activo = true " +
            "AND n.fechaInicio <= :now AND n.fechaFin >= :now")
    Optional<Notice> findCurrentPinnedNotice(@Param("now") LocalDateTime now);

    @Query("SELECT n FROM Notice n WHERE n.author.id = :autorId AND n.activo = true ORDER BY n.createdAt DESC")
    List<Notice> findByAutorId(@Param("autorId") Long autorId);

    @Query("SELECT n FROM Notice n WHERE (LOWER(n.titulo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(n.contenido) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND n.activo = true ORDER BY n.createdAt DESC")
    List<Notice> searchNotices(@Param("searchTerm") String searchTerm);

    @Query("SELECT n FROM Notice n WHERE n.tipo = :tipo AND n.activo = true " +
            "AND n.fechaInicio <= :now AND n.fechaFin >= :now ORDER BY n.prioridad DESC")
    List<Notice> findActiveNoticesByType(@Param("tipo") TipoAviso tipo, @Param("now") LocalDateTime now);
}
