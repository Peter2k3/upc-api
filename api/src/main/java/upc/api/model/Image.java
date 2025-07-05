package upc.api.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "imagenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_original")
    private String nombreOriginal;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "url")
    private String url;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "id_strapi")
    private String idStrapi;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id")
    private User uploadedBy;

    // Relaciones
    @Builder.Default
    @OneToMany(mappedBy = "imagen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PostImage> postImages = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "imagenDestacada", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BlogPost> featuredInPosts = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "imagen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Notice> noticesWithImage = new HashSet<>();
}
