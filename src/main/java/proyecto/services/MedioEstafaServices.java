package proyecto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.dao.MedioEstafaRepository;
import proyecto.entity.MedioEstafa;

@Service
public class MedioEstafaServices {
	
	@Autowired
	private MedioEstafaRepository medEstRepo;
	
	//listar
	public List<MedioEstafa> listaMedioEstafas(){
		return medEstRepo.findAll();
	}
	
    public MedioEstafa obtenerPorId(Integer id) {
        return medEstRepo.findById(id).orElse(null);
    }
}
