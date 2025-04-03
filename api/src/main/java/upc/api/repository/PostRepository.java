package upc.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.api.model.Post;
import upc.api.model.PostStatus;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Encontrar posts por estado con paginación
    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    // Buscar posts por título (ignorando mayúsculas/minúsculas)
    List<Post> findByTitleContainingIgnoreCase(String title);
}
