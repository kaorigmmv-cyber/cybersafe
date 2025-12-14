package proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.dao.UsuarioRepository;
import proyecto.entity.Usuario;

@Service
public class UsuarioServices {
	
	@Autowired
	private UsuarioRepository usuRepository;
	
	public Usuario registrarUsuario(Usuario u) {
	    return usuRepository.save(u);
	}
	
	public Usuario buscarUsuarioPorId(int id) {
		return usuRepository.findById(id).orElse(null);
	}
	
	public boolean correoExiste(String correo) {
	    return usuRepository.existsByCorreoUsuario(correo);
	}
	
	public Usuario buscarPorCorreoUsuario(String correo) {
	    return usuRepository.findByCorreoUsuario(correo);
	}
}

