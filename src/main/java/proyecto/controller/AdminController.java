package proyecto.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import proyecto.services.EstafaServices;
import proyecto.services.MedioEstafaServices;
import proyecto.services.ModalidadEstafaServices;

@Controller
@RequestMapping("dev")
public class AdminController {

	@Autowired
	private EstafaServices estafaServices;
	
	@Autowired
	private ModalidadEstafaServices modEstServices;

	@Autowired
	private MedioEstafaServices medEstServices;
	
	@GetMapping("index")
	public String index() {
		return "index";
	}
	
	@GetMapping("guias")
	public String denuncia() {
		return "denuncia";
	}

	@GetMapping("premium")
	public String premium() {
		return "premium";
	}
	
    // ===============================
    // ESTADÍSTICAS (CYBERSAFE + NACIONAL)
    // ===============================

	// ===============================
    // ESTADÍSTICAS (CYBERSAFE + NACIONAL)
    // ===============================

    @GetMapping("estadisticas")
    public String estadisticas(Model model) {

        int anioActual = LocalDate.now().getYear();

        // ========== CYBERSAFE (BD) ==========

        // Total general
        model.addAttribute("totalEstafas", estafaServices.totalEstafas());

        // Gráficos básicos
        model.addAttribute("estadisticasMedio", estafaServices.estadisticaPorMedio());
        model.addAttribute("estadisticasModalidad", estafaServices.estadisticaPorModalidad());

        // Total del año actual
        model.addAttribute("totalAnio", estafaServices.totalPorAnio(anioActual));

        // Total del mes actual
        model.addAttribute("totalMes", estafaServices.totalPorMesActual());

        // Gráfico de barras por año
        model.addAttribute("reportesPorAnio", estafaServices.reportesPorAnio());

        // Gráfico de línea por meses del año actual
        model.addAttribute("reportesPorMes", estafaServices.reportesPorMes(anioActual));

        // Tendencia histórica año-mes
        model.addAttribute("tendenciaHistorica", estafaServices.tendenciaHistorica());

        // Año actual
        model.addAttribute("anioActual", anioActual);

        return "estadisticas";
    }

    // CYBERSAFE: datos por mes según año (AJAX)
    @GetMapping("estadisticas/meses")
    @ResponseBody
    public List<Object[]> obtenerReportesPorMes(@RequestParam("anio") int anio) {
        return estafaServices.reportesPorMes(anio);
    }
    
    //EN CASO DE INGRESAR A UNA VISTA A LO QUE NO TIENE EL ROL
    @GetMapping("/error/403")
    public String error403() {
        return "error/403";
    }
    
    @GetMapping("/informacionTipos")
    public String mostrarInformacionTipos() {
        return "informacionTipos"; // El nombre del archivo sin la extensión .html
    }
    
    @GetMapping("/tiposEstafa")
    public String mostrarTiposEstafa() {
        return "tiposEstafa"; // El nombre del archivo sin la extensión .html
    }
    @GetMapping("/guiasDenuncia")
    public String mostrarguiasDenuncia() {
        return "guiasDenuncia"; // El nombre del archivo sin la extensión .html
    }

}