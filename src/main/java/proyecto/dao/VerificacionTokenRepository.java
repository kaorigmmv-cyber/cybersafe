package proyecto.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.entity.VerificacionToken;

public interface VerificacionTokenRepository extends JpaRepository<VerificacionToken, Integer>{
    VerificacionToken findByToken(String token);

}
