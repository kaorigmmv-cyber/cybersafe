package proyecto.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.dao.MotivoRechazoRepository;
import proyecto.dto.MotivoRechazoDTO;
import proyecto.entity.MotivoRechazo;

@Service
public class MotivoRechazoServices {

	@Autowired
	private MotivoRechazoRepository motRepository;
	
	public MotivoRechazo registrarEstafa(MotivoRechazo m) {
	    return motRepository.save(m);
	}
	
    // Obtener todos los motivos de rechazo de una estafa
    public List<MotivoRechazo> obtenerMotivosPorEstafaId(Integer estafaId) {
        return motRepository.findByEstafa_CodigoEstafaOrderByFechaRechazoDesc(estafaId);
    }
    
    public List<MotivoRechazoDTO> obtenerMotivosPorEstafaIdDTO(Integer estafaId) {
        return obtenerMotivosPorEstafaId(estafaId).stream()
            .map(m -> {
                MotivoRechazoDTO dto = new MotivoRechazoDTO();
                dto.setMotivoRechazo(m.getMotivoRechazo());
                dto.setFechaRechazo(m.getFechaRechazo());
                return dto;
            }).collect(Collectors.toList());
    }

}