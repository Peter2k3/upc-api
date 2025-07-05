package upc.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import upc.api.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByNombreArchivo(String nombreArchivo);

    Optional<Image> findByIdStrapi(String idStrapi);

    @Query("SELECT i FROM Image i ORDER BY i.createdAt DESC")
    List<Image> findAllActiveImagesOrderedByDate();

    @Query("SELECT i FROM Image i JOIN PostImage pi ON pi.imagen.id = i.id WHERE pi.publicacion.id = :blogPostId")
    List<Image> findImagesByBlogPostId(@Param("blogPostId") Long blogPostId);

    @Query("SELECT COUNT(pi) FROM PostImage pi WHERE pi.imagen.id = :imageId")
    Long countUsageInBlogPosts(@Param("imageId") Long imageId);
}
