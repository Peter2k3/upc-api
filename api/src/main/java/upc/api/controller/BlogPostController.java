package upc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upc.api.model.BlogPost;
import upc.api.model.enums.EstadoPublicacion;
import upc.api.service.IBlogPostService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog-posts")
@CrossOrigin(origins = "*")
public class BlogPostController {

    @Autowired
    private IBlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllBlogPosts() {
        List<BlogPost> posts = blogPostService.getPublishedBlogPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable Long id) {
        Optional<BlogPost> post = blogPostService.getBlogPostById(id);
        return post.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BlogPost> getBlogPostBySlug(@PathVariable String slug) {
        Optional<BlogPost> post = blogPostService.getBlogPostBySlug(slug);
        return post.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<BlogPost>> getFeaturedBlogPosts() {
        List<BlogPost> posts = blogPostService.getFeaturedBlogPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BlogPost>> searchBlogPosts(@RequestParam String q) {
        List<BlogPost> posts = blogPostService.searchBlogPosts(q);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_POSTS')")
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) {
        try {
            if (blogPostService.existsBySlug(blogPost.getSlug())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            BlogPost savedPost = blogPostService.saveBlogPost(blogPost);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_POSTS')")
    public ResponseEntity<BlogPost> updateBlogPost(@PathVariable Long id, @RequestBody BlogPost blogPost) {
        Optional<BlogPost> existingPost = blogPostService.getBlogPostById(id);
        if (existingPost.isPresent()) {
            blogPost.setId(id);
            BlogPost updatedPost = blogPostService.updateBlogPost(blogPost);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_POSTS')")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        Optional<BlogPost> post = blogPostService.getBlogPostById(id);
        if (post.isPresent()) {
            blogPostService.softDeleteBlogPost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAuthority('PUBLISH_POSTS')")
    public ResponseEntity<Void> publishBlogPost(@PathVariable Long id) {
        boolean published = blogPostService.publishBlogPost(id);
        return published ? new ResponseEntity<>(HttpStatus.OK) 
                        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}/unpublish")
    @PreAuthorize("hasAuthority('PUBLISH_POSTS')")
    public ResponseEntity<Void> unpublishBlogPost(@PathVariable Long id) {
        boolean unpublished = blogPostService.unpublishBlogPost(id);
        return unpublished ? new ResponseEntity<>(HttpStatus.OK) 
                          : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('READ_ALL_POSTS')")
    public ResponseEntity<List<BlogPost>> getAllBlogPostsForAdmin() {
        List<BlogPost> posts = blogPostService.getAllActiveBlogPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/by-estado/{estado}")
    @PreAuthorize("hasAuthority('READ_ALL_POSTS')")
    public ResponseEntity<List<BlogPost>> getBlogPostsByEstado(@PathVariable EstadoPublicacion estado) {
        List<BlogPost> posts = blogPostService.getBlogPostsByEstado(estado);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
