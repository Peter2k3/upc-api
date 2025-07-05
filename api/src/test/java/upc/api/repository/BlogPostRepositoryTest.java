package upc.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import upc.api.model.BlogPost;
import upc.api.model.User;
import upc.api.model.enums.EstadoPublicacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class BlogPostRepositoryTest {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    private BlogPost blogPost;
    private User author;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .email("admin@upc.edu")
                .passwordHash("hashedPassword")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        blogPost = BlogPost.builder()
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
    void save_ShouldPersistBlogPost() {
        // Given
        User savedAuthor = userRepository.save(author);
        blogPost.setAuthor(savedAuthor);

        // When
        BlogPost savedPost = blogPostRepository.save(blogPost);

        // Then
        assertNotNull(savedPost);
        assertNotNull(savedPost.getId());
        assertEquals("Test Post", savedPost.getTitulo());
        assertEquals("test-post", savedPost.getSlug());
        assertEquals(EstadoPublicacion.BORRADOR, savedPost.getEstado());
        assertEquals(savedAuthor.getId(), savedPost.getAuthor().getId());
    }

    @Test
    void findBySlug_ShouldReturnPost_WhenExists() {
        // Given
        User savedAuthor = userRepository.save(author);
        blogPost.setAuthor(savedAuthor);
        blogPostRepository.save(blogPost);

        // When
        Optional<BlogPost> foundPost = blogPostRepository.findBySlug("test-post");

        // Then
        assertTrue(foundPost.isPresent());
        assertEquals("Test Post", foundPost.get().getTitulo());
        assertEquals("test-post", foundPost.get().getSlug());
    }

    @Test
    void findBySlug_ShouldReturnEmpty_WhenNotExists() {
        // When
        Optional<BlogPost> foundPost = blogPostRepository.findBySlug("non-existent");

        // Then
        assertFalse(foundPost.isPresent());
    }

    @Test
    void existsBySlug_ShouldReturnTrue_WhenExists() {
        // Given
        User savedAuthor = userRepository.save(author);
        blogPost.setAuthor(savedAuthor);
        blogPostRepository.save(blogPost);

        // When
        boolean exists = blogPostRepository.existsBySlug("test-post");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsBySlug_ShouldReturnFalse_WhenNotExists() {
        // When
        boolean exists = blogPostRepository.existsBySlug("non-existent");

        // Then
        assertFalse(exists);
    }

    @Test
    void findByEstado_ShouldReturnPostsWithSpecificStatus() {
        // Given
        User savedAuthor = userRepository.save(author);
        
        BlogPost publishedPost = BlogPost.builder()
                .titulo("Published Post")
                .contenido("{\"content\": \"Published content\"}")
                .slug("published-post")
                .estado(EstadoPublicacion.PUBLICADO)
                .fechaPublicacion(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(savedAuthor)
                .build();

        BlogPost draftPost = BlogPost.builder()
                .titulo("Draft Post")
                .contenido("{\"content\": \"Draft content\"}")
                .slug("draft-post")
                .estado(EstadoPublicacion.BORRADOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(savedAuthor)
                .build();

        blogPostRepository.save(publishedPost);
        blogPostRepository.save(draftPost);

        // When
        List<BlogPost> publishedPosts = blogPostRepository.findByEstado(EstadoPublicacion.PUBLICADO);
        List<BlogPost> draftPosts = blogPostRepository.findByEstado(EstadoPublicacion.BORRADOR);

        // Then
        assertEquals(1, publishedPosts.size());
        assertEquals(EstadoPublicacion.PUBLICADO, publishedPosts.get(0).getEstado());

        assertEquals(1, draftPosts.size());
        assertEquals(EstadoPublicacion.BORRADOR, draftPosts.get(0).getEstado());
    }

    @Test
    void findPublishedPosts_ShouldReturnOnlyPublishedPosts() {
        // Given
        User savedAuthor = userRepository.save(author);
        
        BlogPost publishedPost = BlogPost.builder()
                .titulo("Published Post")
                .contenido("{\"content\": \"Published content\"}")
                .slug("published-post")
                .estado(EstadoPublicacion.PUBLICADO)
                .fechaPublicacion(LocalDateTime.now().minusDays(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(savedAuthor)
                .build();

        BlogPost futurePost = BlogPost.builder()
                .titulo("Future Post")
                .contenido("{\"content\": \"Future content\"}")
                .slug("future-post")
                .estado(EstadoPublicacion.PUBLICADO)
                .fechaPublicacion(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(savedAuthor)
                .build();

        blogPostRepository.save(publishedPost);
        blogPostRepository.save(futurePost);

        // When
        List<BlogPost> publishedPosts = blogPostRepository.findPublishedPosts(LocalDateTime.now());

        // Then
        assertEquals(1, publishedPosts.size());
        assertEquals("Published Post", publishedPosts.get(0).getTitulo());
        assertTrue(publishedPosts.get(0).getFechaPublicacion().isBefore(LocalDateTime.now()));
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        // Given
        User savedAuthor = userRepository.save(author);
        blogPost.setAuthor(savedAuthor);
        blogPostRepository.save(blogPost);

        BlogPost secondPost = BlogPost.builder()
                .titulo("Second Post")
                .contenido("{\"content\": \"Second content\"}")
                .slug("second-post")
                .estado(EstadoPublicacion.PUBLICADO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(savedAuthor)
                .build();
        blogPostRepository.save(secondPost);

        // When
        long count = blogPostRepository.count();

        // Then
        assertEquals(2, count);
    }
}
