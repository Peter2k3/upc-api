package upc.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import upc.api.model.enums.EstadoPublicacion;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publicaciones_blog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contenido", columnDefinition = "json")
    private String contenido;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPublicacion estado;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imagen_destacada_id")
    private Image imagenDestacada;

    // Relaciones
    @Builder.Default
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PostImage> postImages = new HashSet<>();
}
