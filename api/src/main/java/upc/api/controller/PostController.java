package upc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.api.model.Post;
import upc.api.model.PostStatus;
import upc.api.repository.PostRepository;
import upc.api.service.ImgurService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/upc/v1/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImgurService imgurService;

    // Crear un nuevo post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        try {
            // Si no se establece la fecha de publicación, se establece la actual
            if (post.getPublishedAt() == null) {
                post.setPublishedAt(LocalDateTime.now());
            }

            // Si no se establece el estado, se establece como DRAFT por defecto
            if (post.getStatus() == null) {
                post.setStatus(PostStatus.DRAFT);
            }

            Post savedPost = postRepository.save(post);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los posts (con paginación y ordenamiento)
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "publishedAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction,
        @RequestParam(required = false) PostStatus status) {

        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

            Page<Post> postsPage;
            if (status != null) {
                postsPage = postRepository.findByStatus(status, pageable);
            } else {
                postsPage = postRepository.findAll(pageable);
            }

            List<Post> posts = postsPage.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts);
            response.put("currentPage", postsPage.getNumber());
            response.put("totalItems", postsPage.getTotalElements());
            response.put("totalPages", postsPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un post por ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        Optional<Post> postData = postRepository.findById(id);

        if (postData.isPresent()) {
            return new ResponseEntity<>(postData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un post existente
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody Post post) {
        Optional<Post> postData = postRepository.findById(id);

        if (postData.isPresent()) {
            Post existingPost = postData.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            existingPost.setStatus(post.getStatus());

            if (post.getPublishedAt() != null) {
                existingPost.setPublishedAt(post.getPublishedAt());
            }

            // Solo actualiza la imagen si se proporciona una nueva
            if (post.getFeaturedImage() != null && !post.getFeaturedImage().isEmpty()) {
                existingPost.setFeaturedImage(post.getFeaturedImage());
            }

            return new ResponseEntity<>(postRepository.save(existingPost), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un post
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") Long id) {
        try {
            postRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Publicar un post (cambiar estado a PUBLISHED)
    @PutMapping("/{id}/publish")
    public ResponseEntity<Post> publishPost(@PathVariable("id") Long id) {
        Optional<Post> postData = postRepository.findById(id);

        if (postData.isPresent()) {
            Post post = postData.get();
            post.setStatus(PostStatus.PUBLISHED);
            post.setPublishedAt(LocalDateTime.now());
            return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Archivar un post (cambiar estado a ARCHIVED)
    @PutMapping("/{id}/archive")
    public ResponseEntity<Post> archivePost(@PathVariable("id") Long id) {
        Optional<Post> postData = postRepository.findById(id);

        if (postData.isPresent()) {
            Post post = postData.get();
            post.setStatus(PostStatus.ARCHIVED);
            return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Subir imagen destacada para un post
    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadFeaturedImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (!postOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Subir la imagen a Imgur
            String imageUrl = imgurService.uploadImage(image);

            // Actualizar la entidad Post con la URL de la imagen
            Post post = postOptional.get();
            post.setFeaturedImage(imageUrl);
            postRepository.save(post);

            return ResponseEntity.ok().body(post);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage());
        }
    }

    // Buscar posts por título
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam("title") String title) {
        try {
            List<Post> posts = postRepository.findByTitleContainingIgnoreCase(title);

            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
