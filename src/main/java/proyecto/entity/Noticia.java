package proyecto.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "noticia")
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String titulo;
    @Column(length = 500)
    private String autor;
    @Column(length = 500)
    private String resumen;
    @Column(length = 5500)
    private String contenido;
    @Column(length = 1500)
    private String referencias;
    @Column(length = 500)
    private String urlNoticia;
    @Column(length = 500)
    private String portadaPath;
    @Column(length = 500)
    private LocalDate fechaPublicacion;
    private boolean destacada;
    private String estado;
    private String tipoNoticia;

    // ========================================
    //  CATEGORÍA (ManyToOne)
    // ========================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonIgnore
    private Categoria categoria;

    // ========================================
    // GETTERS & SETTERS
    // ========================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getReferencias() { return referencias; }
    public void setReferencias(String referencias) { this.referencias = referencias; }

    public String getUrlNoticia() { return urlNoticia; }
    public void setUrlNoticia(String urlNoticia) { this.urlNoticia = urlNoticia; }

    public String getPortadaPath() { return portadaPath; }
    public void setPortadaPath(String portadaPath) { this.portadaPath = portadaPath; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public boolean isDestacada() { return destacada; }
    public void setDestacada(boolean destacada) { this.destacada = destacada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoNoticia() { return tipoNoticia; }
    public void setTipoNoticia(String tipoNoticia) { this.tipoNoticia = tipoNoticia; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
