package proyecto.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entity.MotivoRechazo;

public interface MotivoRechazoRepository extends JpaRepository<MotivoRechazo, Integer> {
    // Obtener todos los motivos de rechazo de una estafa
    List<MotivoRechazo> findByEstafa_CodigoEstafaOrderByFechaRechazoDesc(Integer estafaId);
}
