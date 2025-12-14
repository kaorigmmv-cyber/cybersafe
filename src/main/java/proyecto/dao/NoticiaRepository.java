package proyecto.dao;

import proyecto.entity.Categoria;
import proyecto.entity.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    // Buscar noticias por categoría
    List<Noticia> findByCategoria(Categoria categoria);
    Noticia findByTitulo(String titulo);
    
    @Query(
    	    value = "SELECT * FROM noticia " +
    	            "WHERE id <> ?1 " +  // Usamos el parámetro ?1 para el idActual
    	            "AND estado = 'PUBLICADA' " +
    	            "ORDER BY RAND() " +
    	            "LIMIT 9",  // El límite se establece a 5 de manera fija
    	    nativeQuery = true
    	)
    	List<Noticia> buscarNoticiasRecomendadas(Long idActual);  // Ya no es necesario pasar 'limite'
    Noticia findFirstByOrderByFechaPublicacionDesc();
}
