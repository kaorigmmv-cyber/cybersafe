package proyecto.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entity.UsuarioDatos;

public interface UsuarioDatosRepository extends JpaRepository<UsuarioDatos, Integer>{
    boolean existsByDniUsuarioDatos(int dniUsuarioDatos);   
}
