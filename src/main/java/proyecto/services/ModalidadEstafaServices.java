package proyecto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.dao.ModalidadEstafaRepository;
import proyecto.entity.ModalidadEstafa;

@Service
public class ModalidadEstafaServices {

	@Autowired
	private ModalidadEstafaRepository modEstRepo;

	//listar
	public List<ModalidadEstafa> listaModalidadEstafas(){
		return modEstRepo.findAll();
	}
	
    public ModalidadEstafa obtenerPorId(Integer id) {
        return modEstRepo.findById(id).orElse(null);
    }
}


