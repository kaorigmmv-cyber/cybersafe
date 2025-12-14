package proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.dao.RolRepository;
import proyecto.entity.Rol;

@Service
public class RolServices {
	
    @Autowired
    private RolRepository rolRepo;

    public Rol obtenerRolPorNombre(String nombreRol) {
        return rolRepo.findByNombreRol(nombreRol);
    }
    
    public Rol obtenerPorNombre(String nombre) {
        return rolRepo.findByNombreRol(nombre);
    }
}
