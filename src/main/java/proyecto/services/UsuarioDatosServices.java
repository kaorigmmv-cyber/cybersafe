package proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.dao.UsuarioDatosRepository;
import proyecto.entity.UsuarioDatos;

@Service
public class UsuarioDatosServices {
	
	@Autowired
	private UsuarioDatosRepository usuDatosRepository;
	
	public UsuarioDatos registrarUsuarioDatos(UsuarioDatos ud) {
	    return usuDatosRepository.save(ud);
	}
	
	public UsuarioDatos buscarUsuarioDatosPorId(int id) {
		return usuDatosRepository.findById(id).orElse(null);
	}
	
	public boolean dniExiste(int dni) {
	    return usuDatosRepository.existsByDniUsuarioDatos(dni);
	}
}
