package proyecto.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.entity.Cuentas;

public interface CuentasRepository  extends JpaRepository<Cuentas, Integer>{
    Cuentas findByNumeroCuenta(String numeroCuenta);

}
