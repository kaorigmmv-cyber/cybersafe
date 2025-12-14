package proyecto.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entity.EstadoEstafa;

public interface EstadoEstafaRepository extends JpaRepository<EstadoEstafa, Integer>{
    EstadoEstafa findByNombreEstadoEstafa(String nombreEstado);

}
