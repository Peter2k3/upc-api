package upc.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import upc.api.model.enums.TipoAviso;
import upc.api.model.enums.PrioridadAviso;

import java.time.LocalDateTime;

@Entity
@Table(name = "avisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoAviso tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad")
    private PrioridadAviso prioridad;

    @Builder.Default
    @Column(name = "activo")
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "fijado")
    private Boolean fijado = false;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

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
    @JoinColumn(name = "imagen_id")
    private Image imagen;
}
