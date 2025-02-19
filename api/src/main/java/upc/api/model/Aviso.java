package upc.api.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder
@Entity(name = "avisos")
@NoArgsConstructor
@AllArgsConstructor
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFinalizacion;

    @Column(nullable = false)
    private Boolean status; // Activo o inactivo

    @Column(nullable = false)
    private Boolean fijado;

    public boolean isStatus(boolean b) {
        return status;
    }

    public boolean isFijado(boolean b) {
        return fijado;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setFijado(boolean fijado) {
        this.fijado = fijado;
    }
}
