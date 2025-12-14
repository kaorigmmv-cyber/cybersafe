package proyecto.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.entity.Categoria;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);
}
