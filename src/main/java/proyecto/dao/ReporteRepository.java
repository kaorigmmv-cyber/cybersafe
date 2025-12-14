package proyecto.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entity.Reporte;

public interface ReporteRepository  extends JpaRepository<Reporte, Integer>{
	List<Reporte> findByEstafa_CodigoEstafaOrderByCodigoReporteDesc(Integer idEstafa);

}
