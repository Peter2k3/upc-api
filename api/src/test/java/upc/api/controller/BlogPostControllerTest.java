package upc.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import upc.api.model.BlogPost;
import upc.api.model.User;
import upc.api.model.enums.EstadoPublicacion;
import upc.api.service.IBlogPostService;

@WebMvcTest(BlogPostController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlogPostService blogPostService;

    @Autowired
    private ObjectMapper objectMapper;

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
                .estado(EstadoPublicacion.PUBLICADO)
                .fechaPublicacion(LocalDateTime.now().minusDays(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(author)
                .build();
    }

    @Test
    void getAllBlogPosts_ShouldReturnPublishedPosts() throws Exception {
        // Given
        List<BlogPost> posts = Arrays.asList(blogPost);
        when(blogPostService.getPublishedBlogPosts()).thenReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/blog-posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Test Post")))
                .andExpect(jsonPath("$[0].slug", is("test-post")))
                .andExpect(jsonPath("$[0].estado", is("PUBLICADO")));

        verify(blogPostService).getPublishedBlogPosts();
    }

    @Test
    void getBlogPostById_ShouldReturnPost_WhenExists() throws Exception {
        // Given
        when(blogPostService.getBlogPostById(1L)).thenReturn(Optional.of(blogPost));

        // When & Then
        mockMvc.perform(get("/api/blog-posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo", is("Test Post")))
                .andExpect(jsonPath("$.slug", is("test-post")))
                .andExpect(jsonPath("$.estado", is("PUBLICADO")));

        verify(blogPostService).getBlogPostById(1L);
    }

    @Test
    void getBlogPostById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        // Given
        when(blogPostService.getBlogPostById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/blog-posts/1"))
                .andExpect(status().isNotFound());

        verify(blogPostService).getBlogPostById(1L);
    }

    @Test
    void getBlogPostBySlug_ShouldReturnPost_WhenExists() throws Exception {
        // Given
        when(blogPostService.getBlogPostBySlug("test-post")).thenReturn(Optional.of(blogPost));

        // When & Then
        mockMvc.perform(get("/api/blog-posts/slug/test-post"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo", is("Test Post")))
                .andExpect(jsonPath("$.slug", is("test-post")));

        verify(blogPostService).getBlogPostBySlug("test-post");
    }

    @Test
    void getBlogPostBySlug_ShouldReturnNotFound_WhenNotExists() throws Exception {
        // Given
        when(blogPostService.getBlogPostBySlug("non-existent")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/blog-posts/slug/non-existent"))
                .andExpect(status().isNotFound());

        verify(blogPostService).getBlogPostBySlug("non-existent");
    }

    @Test
    void getFeaturedBlogPosts_ShouldReturnFeaturedPosts() throws Exception {
        // Given
        List<BlogPost> featuredPosts = Arrays.asList(blogPost);
        when(blogPostService.getFeaturedBlogPosts()).thenReturn(featuredPosts);

        // When & Then
        mockMvc.perform(get("/api/blog-posts/featured"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Test Post")));

        verify(blogPostService).getFeaturedBlogPosts();
    }

    @Test
    void searchBlogPosts_ShouldReturnMatchingPosts() throws Exception {
        // Given
        List<BlogPost> searchResults = Arrays.asList(blogPost);
        when(blogPostService.searchBlogPosts("test")).thenReturn(searchResults);

        // When & Then
        mockMvc.perform(get("/api/blog-posts/search?q=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Test Post")));

        verify(blogPostService).searchBlogPosts("test");
    }
}
