package upc.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Builder.Default
    @Column(name = "active")
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Campos adicionales para compatibilidad con el código existente
    private String nombres;
    private String paterno;
    private String materno;
    
    @Builder.Default
    private Boolean notExpired = true;
    
    @Builder.Default
    private Boolean notBlocked = true;
    
    @Builder.Default
    private Boolean nonExpiredCredentials = true;

    // Métodos de compatibilidad
    public Long getIdUser() { return this.id; }
    public void setIdUser(Long id) { this.id = id; }
    
    public String getPassword() { return this.passwordHash; }
    public void setPassword(String password) { this.passwordHash = password; }
    
    public LocalDateTime getCreationDate() { return this.createdAt; }
    public void setCreationDate(LocalDateTime creationDate) { this.createdAt = creationDate; }
    
    public LocalDateTime getLastAccess() { return this.lastLogin; }
    public void setLastAccess(LocalDateTime lastAccess) { this.lastLogin = lastAccess; }

    // Método para obtener roles del sistema viejo (ahora solo uno)
    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        if (role != null) {
            roles.add(role);
        }
        return roles;
    }

    // Relaciones - Un usuario tiene un solo rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    private Role role;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserPerson userPerson;

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BlogPost> blogPosts = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Notice> notices = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Image> uploadedImages = new HashSet<>();
}
