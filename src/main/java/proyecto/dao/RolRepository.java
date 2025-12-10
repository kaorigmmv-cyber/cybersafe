package proyecto.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer>{
    Rol findByNombreRol(String nombreRol);
}
