package upc.api.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "paterno")
    private String paterno;

    @Column(name = "materno")
    private String materno;

    @Column(name = "dni", unique = true)
    private String dni;

    @Column(name = "telefono")
    private String telefono;

    @Builder.Default
    @Column(name = "activo")
    private Boolean activo = true;

    // Relaciones
    @Builder.Default
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserPerson> userPersons = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PersonPosition> personPositions = new HashSet<>();
}
