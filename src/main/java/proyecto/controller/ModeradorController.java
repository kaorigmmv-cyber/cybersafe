package proyecto.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import proyecto.dto.MisCasosRegistradosDTO;
import proyecto.entity.EstadoEstafa;
import proyecto.entity.Estafa;
import proyecto.entity.MotivoRechazo;
import proyecto.entity.Reporte;
import proyecto.entity.Usuario;
import proyecto.services.EstadoEstafaServices;
import proyecto.services.EstafaServices;
import proyecto.services.MedioEstafaServices;
import proyecto.services.ModalidadEstafaServices;
import proyecto.services.MotivoRechazoServices;
import proyecto.services.ReporteServices;
import proyecto.services.UsuarioServices;

@Controller
@RequestMapping("mod")
public class ModeradorController {

	@Autowired
	private EstadoEstafaServices estadoEstafaService;
	
	@Autowired
	private ModalidadEstafaServices modEstServices;

	@Autowired
	private MedioEstafaServices medEstServices;
	
	@Autowired
	private EstafaServices estServices;
	
	@Autowired
	private UsuarioServices usuServices;
	
	@Autowired
	private MotivoRechazoServices motRechazoServices;
	
	@Autowired
	private ReporteServices reporteServices;
	
	@GetMapping("revicarCasosRegistrados")
	public String misCasosRegistrados(Model model) {
		model.addAttribute("listadoMedio",medEstServices.listaMedioEstafas());
		model.addAttribute("listadoModalidad",modEstServices.listaModalidadEstafas());
	    return "revisarCasosRegistrados";
	}
	
    @GetMapping("/listarCasosParaRevisar")
    @ResponseBody
    public List<MisCasosRegistradosDTO> obtenerMisCasos(Principal principal) {

        List<Estafa> lista = estServices.listarEstafa(); // este método devuelve todos los casos

        return lista.stream()
                    .map(MisCasosRegistradosDTO::new)
                    .collect(Collectors.toList());
    }
    
    @PostMapping("/rechazarCaso/{id}")
    @ResponseBody
    public ResponseEntity<?> rechazarCaso(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, String> body,
            Principal principal) {

        try {
            // 1. Obtener motivo desde el JSON
            String motivoTexto = body.get("motivo");

            // 2. Buscar la estafa
            Estafa estafa = estServices.buscarPorId(id);
            if (estafa == null) {
                return ResponseEntity.badRequest().body("Estafa no encontrada");
            }

            // 3. Cambiar estado a RECHAZADO
            EstadoEstafa rechazado = estadoEstafaService.buscarPorNombre("Rechazado");
            estafa.setEstadoEstafa(rechazado);
            estServices.registrarEstafa(estafa);

            // 4. Obtener el moderador desde el usuario autenticado
            Usuario moderador = usuServices.buscarPorCorreoUsuario(principal.getName());

            // 5. Crear motivo de rechazo
            MotivoRechazo motivo = new MotivoRechazo();
            motivo.setMotivoRechazo(motivoTexto);
            motivo.setFechaRechazo(LocalDate.now());  // <<< AQUÍ SE PONE LA FECHA
            motivo.setEstafa(estafa);
            motivo.setUsuario(moderador);

            // 6. Guardar motivo
            motRechazoServices.registrarEstafa(motivo);

            return ResponseEntity.ok("Rechazado correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al rechazar");
        }
    }
    
    @PostMapping("/aprobarCaso/{id}")
    @ResponseBody
    public ResponseEntity<?> aprobarCaso(@PathVariable("id") Integer id) {

        try {
            // Buscar la estafa
            Estafa estafa = estServices.buscarPorId(id);
            if (estafa == null) {
                return ResponseEntity.badRequest().body("Estafa no encontrada");
            }

            // Obtener estado APROBADO
            EstadoEstafa aprobado = estadoEstafaService.buscarPorNombre("Aprobado");

            // Cambiar estado
            estafa.setEstadoEstafa(aprobado);

            // Guardar
            estServices.registrarEstafa(estafa);

            return ResponseEntity.ok("Aprobado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al aprobar el caso");
        }
    }
    
    @PostMapping("/bloquearCaso/{id}")
    @ResponseBody
    public ResponseEntity<?> bloquearCaso(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, String> body,
            Principal principal) {

        try {
            // 1. Obtener motivo desde el JSON
            String motivoTexto = body.get("motivo");

            // 2. Buscar la estafa
            Estafa estafa = estServices.buscarPorId(id);
            if (estafa == null) {
                return ResponseEntity.badRequest().body("Estafa no encontrada");
            }

            // 3. Cambiar estado a RECHAZADO
            EstadoEstafa rechazado = estadoEstafaService.buscarPorNombre("Bloqueado");
            estafa.setEstadoEstafa(rechazado);
            estServices.registrarEstafa(estafa);

            // 4. Obtener el moderador desde el usuario autenticado
            Usuario moderador = usuServices.buscarPorCorreoUsuario(principal.getName());

            // 5. Crear motivo de rechazo
            Reporte motivo = new Reporte();
            motivo.setMotivoReporte(motivoTexto);
            motivo.setFechaReporte(LocalDateTime.now());  // <<< AQUÍ SE PONE LA FECHA
            motivo.setEstafa(estafa);
            motivo.setUsuario(moderador);

            // 6. Guardar motivo
            reporteServices.registrarReporte(motivo);

            return ResponseEntity.ok("CASO BLOQUEADO");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("ERROR AL BLOQUEAR");
        }
    }
    
    @PostMapping("/reaprobarCaso/{id}")
    @ResponseBody
    public ResponseEntity<?> reaprobarCaso(@PathVariable("id") Integer id) {

        try {
            // Buscar la estafa
            Estafa estafa = estServices.buscarPorId(id);
            if (estafa == null) {
                return ResponseEntity.badRequest().body("Estafa no encontrada");
            }

            // Obtener estado APROBADO
            EstadoEstafa aprobado = estadoEstafaService.buscarPorNombre("Aprobado");

            // Cambiar estado
            estafa.setEstadoEstafa(aprobado);

            // Guardar
            estServices.registrarEstafa(estafa);

            return ResponseEntity.ok("Aprobado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al aprobar el caso");
        }
    }
    
    @GetMapping("/reporte/por-estafa/{id}")
    public ResponseEntity<?> listarReportesPorEstafa(@PathVariable Integer id) {
        return ResponseEntity.ok(reporteServices.obtenerReportesPorEstafa(id));
    }

}
