package upc.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import upc.api.model.BlogPost;
import upc.api.model.User;
import upc.api.model.enums.EstadoPublicacion;
import upc.api.repository.BlogPostRepository;
import upc.api.repository.ImageRepository;
import upc.api.repository.PostImageRepository;
import upc.api.service.impl.BlogPostServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogPostServiceTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    private BlogPost blogPost;
    private User author;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .id(1L)
                .email("admin@upc.edu")
                .active(true)
                .build();

        blogPost = BlogPost.builder()
                .id(1L)
                .titulo("Test Post")
                .contenido("{\"content\": \"Test content\"}")
                .slug("test-post")
                .estado(EstadoPublicacion.BORRADOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(author)
                .build();
    }

    @Test
    void saveBlogPost_ShouldReturnSavedPost() {
        // Given
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(blogPost);

        // When
        BlogPost savedPost = blogPostService.saveBlogPost(blogPost);

        // Then
        assertNotNull(savedPost);
        assertEquals("Test Post", savedPost.getTitulo());
        assertEquals("test-post", savedPost.getSlug());
        verify(blogPostRepository).save(any(BlogPost.class));
    }

    @Test
    void getBlogPostById_ShouldReturnPost_WhenExists() {
        // Given
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        // When
        Optional<BlogPost> result = blogPostService.getBlogPostById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Post", result.get().getTitulo());
        verify(blogPostRepository).findById(1L);
    }

    @Test
    void getBlogPostBySlug_ShouldReturnPost_WhenExists() {
        // Given
        when(blogPostRepository.findBySlug("test-post")).thenReturn(Optional.of(blogPost));

        // When
        Optional<BlogPost> result = blogPostService.getBlogPostBySlug("test-post");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test-post", result.get().getSlug());
        verify(blogPostRepository).findBySlug("test-post");
    }

    @Test
    void getPublishedBlogPosts_ShouldReturnPublishedPosts() {
        // Given
        blogPost.setEstado(EstadoPublicacion.PUBLICADO);
        blogPost.setFechaPublicacion(LocalDateTime.now().minusDays(1));
        List<BlogPost> publishedPosts = Arrays.asList(blogPost);
        when(blogPostRepository.findPublishedPosts(any(LocalDateTime.class))).thenReturn(publishedPosts);

        // When
        List<BlogPost> result = blogPostService.getPublishedBlogPosts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(EstadoPublicacion.PUBLICADO, result.get(0).getEstado());
        verify(blogPostRepository).findPublishedPosts(any(LocalDateTime.class));
    }

    @Test
    void publishBlogPost_ShouldChangeStatusToPublished() {
        // Given
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(blogPost);

        // When
        boolean result = blogPostService.publishBlogPost(1L);

        // Then
        assertTrue(result);
        assertEquals(EstadoPublicacion.PUBLICADO, blogPost.getEstado());
        assertNotNull(blogPost.getFechaPublicacion());
        verify(blogPostRepository).save(blogPost);
    }

    @Test
    void publishBlogPost_ShouldReturnFalse_WhenPostNotExists() {
        // Given
        when(blogPostRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        boolean result = blogPostService.publishBlogPost(1L);

        // Then
        assertFalse(result);
        verify(blogPostRepository, never()).save(any(BlogPost.class));
    }

    @Test
    void generateSlug_ShouldCreateValidSlug() {
        // When
        String slug = blogPostService.generateSlug("Test Post Title");

        // Then
        assertEquals("test-post-title", slug);
    }

    @Test
    void generateSlug_ShouldHandleSpecialCharacters() {
        // When
        String slug = blogPostService.generateSlug("Test Post! With @Special #Characters");

        // Then
        assertEquals("test-post-with-special-characters", slug);
    }

    @Test
    void existsBySlug_ShouldReturnTrue_WhenExists() {
        // Given
        when(blogPostRepository.existsBySlug("test-post")).thenReturn(true);

        // When
        boolean exists = blogPostService.existsBySlug("test-post");

        // Then
        assertTrue(exists);
        verify(blogPostRepository).existsBySlug("test-post");
    }

    @Test
    void searchBlogPosts_ShouldReturnMatchingPosts() {
        // Given
        List<BlogPost> searchResults = Arrays.asList(blogPost);
        when(blogPostRepository.searchPublishedPosts("test")).thenReturn(searchResults);

        // When
        List<BlogPost> result = blogPostService.searchBlogPosts("test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blogPostRepository).searchPublishedPosts("test");
    }
}
