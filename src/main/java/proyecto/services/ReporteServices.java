package proyecto.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import proyecto.dao.EstafaRepository;
import proyecto.dao.ReporteRepository;
import proyecto.dao.UsuarioRepository;
import proyecto.entity.Estafa;
import proyecto.entity.MotivoRechazo;
import proyecto.entity.Reporte;
import proyecto.entity.Usuario;

@Service
public class ReporteServices {
    
	@Autowired
	private ReporteRepository reporteRepository;
	
    @Autowired
    private EstafaRepository estafaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Reporte registrarReporte(Integer idEstafa, String motivo) {

        // Obtener al usuario logeado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreoUsuario(username);

        Estafa estafa = estafaRepository.findById(idEstafa)
                .orElseThrow(() -> new RuntimeException("Estafa no encontrada"));

        Reporte r = new Reporte();
        r.setEstafa(estafa);
        r.setUsuario(usuario);
        r.setMotivoReporte(motivo);
        r.setFechaReporte(LocalDateTime.now());

        return reporteRepository.save(r);
    }
    
	public Reporte registrarReporte(Reporte r) {
	    return reporteRepository.save(r);
	}    
	
	public List<Reporte> obtenerReportesPorEstafa(Integer idEstafa) {
	    return reporteRepository.findByEstafa_CodigoEstafaOrderByCodigoReporteDesc(idEstafa);
	}
}
