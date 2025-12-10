package proyecto.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.dao.EstafaRepository;
import proyecto.entity.Estafa;

@Service
public class EstafaServices {
	
	@Autowired
	private EstafaRepository estafaRepo;
	
	
	//listar
	public List<Estafa> listarEstafa(){
		return estafaRepo.findAll();
	}
	
	public Estafa registrarEstafa(Estafa p) {
	    return estafaRepo.save(p);
	}
	
    // Buscar estafa por ID
    public Estafa buscarPorId(Integer id) {
        return estafaRepo.findById(id).orElse(null);
    }
    
    public List<Estafa> listarPorUsuario(Integer idUsuario) {
        return estafaRepo.findByUsuarioCodigoUsuario(idUsuario);
    }
    
    //LISTAR CASOS DONDE EL ESTADO SEA APROBADO, PARA LA VISTA PARA TODOS
    public List<Estafa> listarEstafasAprobadas() {
        return estafaRepo.findByEstadoEstafa_CodigoEstadoEstafa(2);
    }
    
    // ==========================
    // ESTADÍSTICAS BÁSICAS
    // ==========================

    public long totalEstafas() {
        return estafaRepo.count();
    }

    public List<Object[]> estadisticaPorMedio() {
        return estafaRepo.conteoPorMedio();
    }

    public List<Object[]> estadisticaPorModalidad() {
        return estafaRepo.conteoPorModalidad();
    }
    
    // ==========================
    // 📊 ESTADÍSTICAS AVANZADAS
    // ==========================


    // 📌 1️⃣ Reportes por año (últimos años dinámicos)
    public List<Object[]> reportesPorAnio() {
        return estafaRepo.conteoPorAnio();
    }


    // 📌 2️⃣ Total de reportes del año actual
    public long totalPorAnio(int anio) {
        return estafaRepo.totalPorAnio(anio);
    }


    // 📌 3️⃣ Total del mes actual
    public long totalPorMesActual() {
        int anio = LocalDate.now().getYear();
        int mes  = LocalDate.now().getMonthValue();
        return estafaRepo.totalPorMes(anio, mes);
    }


    // 📌 4️⃣ Reportes por cada mes del año actual
    public List<Object[]> reportesPorMesActual() {
        int anio = LocalDate.now().getYear();
        return estafaRepo.reportesPorMes(anio);
    }


    // 📌 5️⃣ Reportes por mes de un año seleccionado
    public List<Object[]> reportesPorMes(int anio) {
        return estafaRepo.reportesPorMes(anio);
    }


    // 📌 6️⃣ Tendencia histórica año-mes
    public List<Object[]> tendenciaHistorica() {
        return estafaRepo.tendenciaHistorica();
    }
}
