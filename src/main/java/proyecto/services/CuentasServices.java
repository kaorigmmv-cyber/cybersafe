package proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.dao.CuentasRepository;
import proyecto.entity.Cuentas;

@Service
public class CuentasServices {
	 @Autowired
	    private CuentasRepository cuentasRepository;

	    public Cuentas buscarPorNumero(String numero) {
	        return cuentasRepository.findByNumeroCuenta(numero);
	    }

	    public void guardar(Cuentas cuenta) {
	        cuentasRepository.save(cuenta);
	    }
}
