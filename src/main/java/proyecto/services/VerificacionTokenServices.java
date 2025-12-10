package proyecto.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.dao.UsuarioRepository;
import proyecto.dao.VerificacionTokenRepository;
import proyecto.entity.Usuario;
import proyecto.entity.VerificacionToken;

@Service
public class VerificacionTokenServices {
	
	@Autowired
    private VerificacionTokenRepository tokenRepo;
	
    @Autowired
    private UsuarioRepository usuarioRepo;
	
	public String crearTokenParaUsuario(Usuario usuario) {

        String token = UUID.randomUUID().toString();

        VerificacionToken verToken = new VerificacionToken();
        verToken.setToken(token);
        verToken.setUsuario(usuario);
        verToken.setFechaExpiracionToken(LocalDateTime.now().plusHours(900));

        tokenRepo.save(verToken);

        return token;
    }
	
    public boolean activarCuenta(String token) {

        VerificacionToken tokenObj = tokenRepo.findByToken(token);

        if (tokenObj == null) {
            return false; // token no existe
        }

        // VALIDAR EXPIRACIÓN
        if (tokenObj.getFechaExpiracionToken().isBefore(LocalDateTime.now())) {
            tokenRepo.delete(tokenObj);
            return false;
        }
        
        Usuario usuario = tokenObj.getUsuario();

        if (usuario == null) {
            return false;
        }

        // ACTIVAR
        usuario.setActivadoUsuario(true);

        usuarioRepo.save(usuario);  // <--- IMPORTANTE

        // OPCIONAL: eliminar token después de usarlo
        tokenRepo.delete(tokenObj);

        return true;
    }
}
