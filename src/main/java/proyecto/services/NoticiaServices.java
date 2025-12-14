
package proyecto.services;

import proyecto.entity.Noticia;
import proyecto.dao.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticiaServices {

    @Autowired
    private NoticiaRepository noticiaRepository;

    // Obtener una noticia por su ID
    public Noticia obtenerNoticia(Long id) {
        Optional<Noticia> noticia = noticiaRepository.findById(id);
        return noticia.orElse(null); // Retorna la noticia si existe, o null si no
    }

    // Obtener todas las noticias
    public List<Noticia> obtenerTodasLasNoticias() {
        return noticiaRepository.findAll();
    }

    public Noticia buscarPorTitulo(String titulo) {
        return noticiaRepository.findByTitulo(titulo);
    }

    // Crear o actualizar una noticia
    public Noticia guardarNoticia(Noticia noticia) {
        return noticiaRepository.save(noticia);
    }

    // Eliminar una noticia
    public void eliminarNoticia(Long id) {
        noticiaRepository.deleteById(id);
    }
    public Noticia obtenerPorId(Long id) {
        return noticiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));
    }
    public List<Noticia> obtenerNoticiasRecomendadas(Long idActual) {
        return noticiaRepository.buscarNoticiasRecomendadas(idActual);  // Solo pasamos el idActual
    }


    // Método para obtener la última noticia destacada
    public Noticia obtenerUltimaNoticiaDestacada() {
        // Lógica para obtener la última noticia destacada
        return noticiaRepository.findFirstByOrderByFechaPublicacionDesc(); // Ejemplo
    }

}
