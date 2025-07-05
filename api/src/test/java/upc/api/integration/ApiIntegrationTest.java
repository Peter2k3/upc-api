package upc.api.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import upc.api.model.Role;
import upc.api.model.Permission;
import upc.api.model.User;
import upc.api.model.BlogPost;
import upc.api.model.enums.EstadoPublicacion;
import upc.api.service.IRoleService;
import upc.api.service.IPermissionService;
import upc.api.service.IUserService;
import upc.api.service.IBlogPostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@Transactional
class ApiIntegrationTest {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBlogPostService blogPostService;

    private Role adminRole;
    private Permission readPermission;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Crear rol
        adminRole = Role.builder()
                .name("ADMIN")
                .description("Administrator role")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Crear permiso
        readPermission = Permission.builder()
                .name("READ_POSTS")
                .description("Read blog posts")
                .resource("posts")
                .action("READ")
                .createdAt(LocalDateTime.now())
                .build();

        // Crear usuario
        adminUser = User.builder()
                .email("admin@upc.edu")
                .passwordHash("hashedPassword")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void completeWorkflow_ShouldCreateAndManageEntities() {
        // 1. Crear y guardar rol
        Role savedRole = roleService.saveRole(adminRole);
        assertNotNull(savedRole.getId());
        assertEquals("ADMIN", savedRole.getName());

        // 2. Crear y guardar permiso
        Permission savedPermission = permissionService.savePermission(readPermission);
        assertNotNull(savedPermission.getId());
        assertEquals("READ_POSTS", savedPermission.getName());

        // 3. Asignar permiso a rol
        boolean assigned = roleService.assignPermissionToRole(savedRole.getId(), savedPermission.getId());
        assertTrue(assigned);

        // 4. Verificar que el permiso se asignó correctamente
        List<Permission> rolePermissions = roleService.getPermissionsByRole(savedRole.getId());
        assertEquals(1, rolePermissions.size());
        assertEquals("READ_POSTS", rolePermissions.get(0).getName());

        // 5. Crear usuario con rol
        adminUser.setRole(savedRole);
        User savedUser = userService.saveUser(adminUser);
        assertNotNull(savedUser.getId());
        assertEquals("admin@upc.edu", savedUser.getEmail());
        assertEquals(savedRole.getId(), savedUser.getRole().getId());

        // 6. Crear blog post
        BlogPost blogPost = BlogPost.builder()
                .titulo("Test Integration Post")
                .contenido("{\"content\": \"Integration test content\", \"images\": [\"img1.jpg\", \"img2.jpg\"]}")
                .slug("test-integration-post")
                .estado(EstadoPublicacion.BORRADOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(savedUser)
                .build();

        BlogPost savedPost = blogPostService.saveBlogPost(blogPost);
        assertNotNull(savedPost.getId());
        assertEquals("Test Integration Post", savedPost.getTitulo());
        assertEquals("test-integration-post", savedPost.getSlug());
        assertEquals(EstadoPublicacion.BORRADOR, savedPost.getEstado());

        // 7. Publicar post
        boolean published = blogPostService.publishBlogPost(savedPost.getId());
        assertTrue(published);

        // 8. Verificar que el post se publicó
        Optional<BlogPost> publishedPost = blogPostService.getBlogPostById(savedPost.getId());
        assertTrue(publishedPost.isPresent());
        assertEquals(EstadoPublicacion.PUBLICADO, publishedPost.get().getEstado());
        assertNotNull(publishedPost.get().getFechaPublicacion());

        // 9. Buscar posts publicados
        List<BlogPost> publishedPosts = blogPostService.getPublishedBlogPosts();
        assertEquals(1, publishedPosts.size());
        assertEquals("Test Integration Post", publishedPosts.get(0).getTitulo());

        // 10. Buscar posts por slug
        Optional<BlogPost> foundBySlug = blogPostService.getBlogPostBySlug("test-integration-post");
        assertTrue(foundBySlug.isPresent());
        assertEquals("Test Integration Post", foundBySlug.get().getTitulo());

        // 11. Verificar que el contenido JSON contiene imágenes
        String content = foundBySlug.get().getContenido();
        assertTrue(content.contains("img1.jpg"));
        assertTrue(content.contains("img2.jpg"));

        // 12. Buscar posts por autor
        List<BlogPost> authorPosts = blogPostService.getBlogPostsByAutor(savedUser.getId());
        assertEquals(1, authorPosts.size());
        assertEquals(savedUser.getId(), authorPosts.get(0).getAuthor().getId());
    }

    @Test
    void roleAndPermissionManagement_ShouldWork() {
        // 1. Crear múltiples roles
        Role userRole = Role.builder()
                .name("USER")
                .description("Regular user role")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Role savedAdminRole = roleService.saveRole(adminRole);
        Role savedUserRole = roleService.saveRole(userRole);

        // 2. Crear múltiples permisos
        Permission writePermission = Permission.builder()
                .name("WRITE_POSTS")
                .description("Write blog posts")
                .resource("posts")
                .action("WRITE")
                .createdAt(LocalDateTime.now())
                .build();

        Permission savedReadPermission = permissionService.savePermission(readPermission);
        Permission savedWritePermission = permissionService.savePermission(writePermission);

        // 3. Asignar permisos a roles
        assertTrue(roleService.assignPermissionToRole(savedAdminRole.getId(), savedReadPermission.getId()));
        assertTrue(roleService.assignPermissionToRole(savedAdminRole.getId(), savedWritePermission.getId()));
        assertTrue(roleService.assignPermissionToRole(savedUserRole.getId(), savedReadPermission.getId()));

        // 4. Verificar permisos de admin
        List<Permission> adminPermissions = roleService.getPermissionsByRole(savedAdminRole.getId());
        assertEquals(2, adminPermissions.size());

        // 5. Verificar permisos de usuario
        List<Permission> userPermissions = roleService.getPermissionsByRole(savedUserRole.getId());
        assertEquals(1, userPermissions.size());
        assertEquals("READ_POSTS", userPermissions.get(0).getName());

        // 6. Verificar que no se pueden asignar permisos duplicados
        assertFalse(roleService.assignPermissionToRole(savedAdminRole.getId(), savedReadPermission.getId()));

        // 7. Remover permiso
        assertTrue(roleService.removePermissionFromRole(savedAdminRole.getId(), savedWritePermission.getId()));
        List<Permission> updatedPermissions = roleService.getPermissionsByRole(savedAdminRole.getId());
        assertEquals(1, updatedPermissions.size());
    }

    @Test
    void blogPostLifecycle_ShouldWork() {
        // 1. Crear usuario autor
        Role authorRole = roleService.saveRole(adminRole);
        adminUser.setRole(authorRole);
        User savedAuthor = userService.saveUser(adminUser);

        // 2. Crear post como borrador
        BlogPost blogPost = BlogPost.builder()
                .titulo("Lifecycle Test Post")
                .contenido("{\"content\": \"Lifecycle test content\"}")
                .slug("lifecycle-test-post")
                .estado(EstadoPublicacion.BORRADOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(savedAuthor)
                .build();

        BlogPost savedPost = blogPostService.saveBlogPost(blogPost);

        // 3. Verificar que no aparece en posts publicados
        List<BlogPost> publishedPosts = blogPostService.getPublishedBlogPosts();
        assertEquals(0, publishedPosts.size());

        // 4. Publicar post
        blogPostService.publishBlogPost(savedPost.getId());

        // 5. Verificar que aparece en posts publicados
        List<BlogPost> publishedPostsAfter = blogPostService.getPublishedBlogPosts();
        assertEquals(1, publishedPostsAfter.size());

        // 6. Despublicar post
        blogPostService.unpublishBlogPost(savedPost.getId());

        // 7. Verificar que no aparece en posts publicados
        List<BlogPost> publishedPostsFinal = blogPostService.getPublishedBlogPosts();
        assertEquals(0, publishedPostsFinal.size());

        // 8. Verificar que sigue existiendo como borrador
        Optional<BlogPost> draftPost = blogPostService.getBlogPostById(savedPost.getId());
        assertTrue(draftPost.isPresent());
        assertEquals(EstadoPublicacion.BORRADOR, draftPost.get().getEstado());
    }

    @Test
    void slugGeneration_ShouldWork() {
        // Test slug generation
        String slug1 = blogPostService.generateSlug("Test Post Title");
        assertEquals("test-post-title", slug1);

        String slug2 = blogPostService.generateSlug("Post with Special Characters!@#$%");
        assertEquals("post-with-special-characters", slug2);

        String slug3 = blogPostService.generateSlug("Multiple    Spaces    Between    Words");
        assertEquals("multiple-spaces-between-words", slug3);
    }
}
