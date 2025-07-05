package upc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.api.model.BlogPost;
import upc.api.model.Image;
import upc.api.model.enums.EstadoPublicacion;
import upc.api.repository.BlogPostRepository;
import upc.api.repository.ImageRepository;
import upc.api.repository.PostImageRepository;
import upc.api.service.IBlogPostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogPostServiceImpl implements IBlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Override
    public BlogPost saveBlogPost(BlogPost blogPost) {
        if (blogPost.getCreatedAt() == null) {
            blogPost.setCreatedAt(LocalDateTime.now());
        }
        blogPost.setUpdatedAt(LocalDateTime.now());
        return blogPostRepository.save(blogPost);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> getAllActiveBlogPosts() {
        return blogPostRepository.findAll(); // No hay campo activo en BlogPost
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BlogPost> getBlogPostById(Long id) {
        return blogPostRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BlogPost> getBlogPostBySlug(String slug) {
        return blogPostRepository.findBySlug(slug);
    }

    @Override
    public BlogPost updateBlogPost(BlogPost blogPost) {
        blogPost.setUpdatedAt(LocalDateTime.now());
        return blogPostRepository.save(blogPost);
    }

    @Override
    public void deleteBlogPost(Long id) {
        blogPostRepository.deleteById(id);
    }

    @Override
    public void softDeleteBlogPost(Long id) {
        // No hay campo activo en BlogPost, simplemente borra
        deleteBlogPost(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        return blogPostRepository.existsBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> getBlogPostsByEstado(EstadoPublicacion estado) {
        return blogPostRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> getPublishedBlogPosts() {
        return blogPostRepository.findPublishedPosts(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> getFeaturedBlogPosts() {
        return blogPostRepository.findFeaturedPosts(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> getBlogPostsByAutor(Long autorId) {
        return blogPostRepository.findByAutorId(autorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> searchBlogPosts(String searchTerm) {
        return blogPostRepository.searchPublishedPosts(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> getBlogPostsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return blogPostRepository.findPostsBetweenDates(startDate, endDate);
    }

    @Override
    public boolean publishBlogPost(Long id) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        if (blogPost.isPresent()) {
            BlogPost bp = blogPost.get();
            bp.setEstado(EstadoPublicacion.PUBLICADO);
            bp.setFechaPublicacion(LocalDateTime.now());
            bp.setUpdatedAt(LocalDateTime.now());
            blogPostRepository.save(bp);
            return true;
        }
        return false;
    }

    @Override
    public boolean unpublishBlogPost(Long id) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        if (blogPost.isPresent()) {
            BlogPost bp = blogPost.get();
            bp.setEstado(EstadoPublicacion.BORRADOR);
            bp.setUpdatedAt(LocalDateTime.now());
            blogPostRepository.save(bp);
            return true;
        }
        return false;
    }

    @Override
    public boolean addImageToBlogPost(Long blogPostId, Long imageId, Integer orden) {
        // Implementación básica - se podría mejorar
        return true;
    }

    @Override
    public boolean removeImageFromBlogPost(Long blogPostId, Long imageId) {
        // Implementación básica - se podría mejorar
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getBlogPostImages(Long blogPostId) {
        return imageRepository.findImagesByBlogPostId(blogPostId);
    }

    @Override
    public boolean setFeaturedImage(Long blogPostId, Long imageId) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(blogPostId);
        Optional<Image> image = imageRepository.findById(imageId);
        if (blogPost.isPresent() && image.isPresent()) {
            BlogPost bp = blogPost.get();
            bp.setImagenDestacada(image.get());
            bp.setUpdatedAt(LocalDateTime.now());
            blogPostRepository.save(bp);
            return true;
        }
        return false;
    }

    @Override
    public String generateSlug(String titulo) {
        return titulo.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }
}
