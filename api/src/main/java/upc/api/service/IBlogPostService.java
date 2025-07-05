package upc.api.service;

import upc.api.model.BlogPost;
import upc.api.model.Image;
import upc.api.model.enums.EstadoPublicacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IBlogPostService {
    
    BlogPost saveBlogPost(BlogPost blogPost);
    
    List<BlogPost> getAllBlogPosts();
    
    List<BlogPost> getAllActiveBlogPosts();
    
    Optional<BlogPost> getBlogPostById(Long id);
    
    Optional<BlogPost> getBlogPostBySlug(String slug);
    
    BlogPost updateBlogPost(BlogPost blogPost);
    
    void deleteBlogPost(Long id);
    
    void softDeleteBlogPost(Long id);
    
    boolean existsBySlug(String slug);
    
    List<BlogPost> getBlogPostsByEstado(EstadoPublicacion estado);
    
    List<BlogPost> getPublishedBlogPosts();
    
    List<BlogPost> getFeaturedBlogPosts();
    
    List<BlogPost> getBlogPostsByAutor(Long autorId);
    
    List<BlogPost> searchBlogPosts(String searchTerm);
    
    List<BlogPost> getBlogPostsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    boolean publishBlogPost(Long id);
    
    boolean unpublishBlogPost(Long id);
    
    boolean addImageToBlogPost(Long blogPostId, Long imageId, Integer orden);
    
    boolean removeImageFromBlogPost(Long blogPostId, Long imageId);
    
    List<Image> getBlogPostImages(Long blogPostId);
    
    boolean setFeaturedImage(Long blogPostId, Long imageId);
    
    String generateSlug(String titulo);
}
