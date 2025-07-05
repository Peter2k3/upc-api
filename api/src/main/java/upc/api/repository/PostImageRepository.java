package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.PostImage;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findByPublicacion_Id(Long blogPostId);

    List<PostImage> findByImagen_Id(Long imageId);

    Optional<PostImage> findByPublicacion_IdAndImagen_Id(Long blogPostId, Long imageId);

    boolean existsByPublicacion_IdAndImagen_Id(Long blogPostId, Long imageId);

    @Query("SELECT pi FROM PostImage pi WHERE pi.publicacion.id = :blogPostId ORDER BY pi.orden ASC")
    List<PostImage> findByBlogPostIdOrderByOrden(@Param("blogPostId") Long blogPostId);

    @Query("SELECT pi FROM PostImage pi WHERE pi.imagen.id = :imageId AND pi.publicacion.estado = 'PUBLICADO'")
    List<PostImage> findActivePostsByImageId(@Param("imageId") Long imageId);

    @Query("SELECT MAX(pi.orden) FROM PostImage pi WHERE pi.publicacion.id = :blogPostId")
    Integer findMaxOrdenByBlogPostId(@Param("blogPostId") Long blogPostId);
}
