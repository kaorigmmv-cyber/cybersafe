package proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.dao.EstadoEstafaRepository;
import proyecto.entity.EstadoEstafa;

@Service
public class EstadoEstafaServices {

	
	@Autowired
	private EstadoEstafaRepository estEstRepository;
	
    public EstadoEstafa buscarPorNombre(String nombre) {
        return estEstRepository.findByNombreEstadoEstafa(nombre);
    }
}
