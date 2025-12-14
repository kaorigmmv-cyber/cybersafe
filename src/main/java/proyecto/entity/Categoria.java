package proyecto.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(length = 400, nullable = false, unique = true)
    @JsonIgnore
    private String nombre;

    @Column(length = 100, unique = true)
    private String slug;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private Set<Noticia> noticias = new HashSet<>();

    // ===== Constructores =====

    public Categoria() { }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // ===== Getters y Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public Set<Noticia> getNoticias() { return noticias; }
    public void setNoticias(Set<Noticia> noticias) { this.noticias = noticias; }
}
