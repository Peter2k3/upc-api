package upc.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.BlogPost;
import upc.api.model.enums.EstadoPublicacion;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Optional<BlogPost> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<BlogPost> findByEstado(EstadoPublicacion estado);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.estado = :estado ORDER BY bp.fechaPublicacion DESC")
    List<BlogPost> findByEstadoOrderByFechaPublicacionDesc(@Param("estado") EstadoPublicacion estado);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.estado = 'PUBLICADO' " +
            "AND bp.fechaPublicacion <= :now ORDER BY bp.fechaPublicacion DESC")
    List<BlogPost> findPublishedPosts(@Param("now") LocalDateTime now);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.imagenDestacada IS NOT NULL AND bp.estado = 'PUBLICADO' " +
            "AND bp.fechaPublicacion <= :now ORDER BY bp.fechaPublicacion DESC")
    List<BlogPost> findFeaturedPosts(@Param("now") LocalDateTime now);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.author.id = :autorId ORDER BY bp.createdAt DESC")
    List<BlogPost> findByAutorId(@Param("autorId") Long autorId);

    @Query("SELECT bp FROM BlogPost bp WHERE LOWER(bp.titulo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND bp.estado = 'PUBLICADO' ORDER BY bp.fechaPublicacion DESC")
    List<BlogPost> searchPublishedPosts(@Param("searchTerm") String searchTerm);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.fechaPublicacion BETWEEN :startDate AND :endDate " +
            "AND bp.estado = 'PUBLICADO' ORDER BY bp.fechaPublicacion DESC")
    List<BlogPost> findPostsBetweenDates(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
