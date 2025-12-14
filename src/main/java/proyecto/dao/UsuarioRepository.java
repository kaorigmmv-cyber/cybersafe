package proyecto.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    boolean existsByCorreoUsuario(String correoUsuario);
    Usuario findByCorreoUsuario(String correo);

}
