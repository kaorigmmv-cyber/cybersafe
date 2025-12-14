package proyecto.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import proyecto.dto.EstafaDTO;
import proyecto.dto.MotivoRechazoDTO;
import proyecto.dto.ReporteDTO;
import proyecto.entity.EstadoEstafa;
import proyecto.entity.Estafa;
import proyecto.entity.MedioEstafa;
import proyecto.entity.ModalidadEstafa;
import proyecto.entity.MotivoRechazo;
import proyecto.entity.Reporte;
import proyecto.entity.Usuario;
import proyecto.services.CorreoServices;
import proyecto.services.EstafaServices;
import proyecto.services.MedioEstafaServices;
import proyecto.services.ModalidadEstafaServices;
import proyecto.services.MotivoRechazoServices;
import proyecto.services.ReporteServices;
import proyecto.services.UsuarioServices;

@Controller
@RequestMapping("est")
public class EstafaController {
	
	@Autowired
	private EstafaServices estafaServices;
	
	@Autowired
	private ModalidadEstafaServices modEstServices;

	@Autowired
	private MedioEstafaServices medEstServices;
	
	@Autowired
	private UsuarioServices usuServices;
	
	@Autowired
	private CorreoServices corServices;
	
	@Autowired
	private MotivoRechazoServices motRechazoServices;
	
	@Autowired
	private ReporteServices reporServices;
	
	@Autowired
	private Cloudinary cloudinary;
	
	@GetMapping("/registrarCiberdelito")
	public String registrarCiberdelito(Model model, Principal principal) {

	    Usuario usuario = usuServices.buscarPorCorreoUsuario(principal.getName());
	    model.addAttribute("contador", usuario.getContadorIntentos());

		model.addAttribute("listadoMedio",medEstServices.listaMedioEstafas());
		model.addAttribute("listadoModalidad",modEstServices.listaModalidadEstafas());
	    return "registrarciberdelito";
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<String> registrar(
	        @RequestParam("titulo") String titulo,
	        @RequestParam("descripcion") String descripcion,
	        @RequestParam("imagen") MultipartFile archivo,  // <--- CAMBIO IMPORTANTE
	        @RequestParam("ciberdelincuente") String ciberdelincuente,
	        @RequestParam("codigoMedio") int medio,
	        @RequestParam("codigoModalidad") int modalidad,
	        @RequestParam(value = "formal", required = false) Boolean formal,

	        Principal principal
	) {

	    try {
	    	
	    	 // OBTENER EMAIL DEL USUARIO LOGEADO
	        String correo = principal.getName();

	        // BUSCAR EL OBJETO USUARIO EN BD
	        Usuario usuario = usuServices.buscarPorCorreoUsuario(correo);
	        
	        // --- SUBIR IMAGEN A CLOUDINARY ---
	        Map upload = cloudinary.uploader().upload(archivo.getBytes(), ObjectUtils.emptyMap());
	        String urlImagen = upload.get("secure_url").toString();

	        //GUARDAMOS REGISTRO
	        Estafa obj = new Estafa();
	        obj.setTituloCaso(titulo);
	        obj.setDescripcionEstafa(descripcion);
	        obj.setImagenEstafa(urlImagen);
	        obj.setCiberdelincuente(ciberdelincuente);
	        obj.setContadorReportes(0);
            obj.setFechaReporte(LocalDate.now());
			/*
			 * MedioEstafa m = new MedioEstafa(); m.setCodigoMedioEstafa(medio);
			 * obj.setMedioEstafa(m);
			 * 
			 * ModalidadEstafa mod = new ModalidadEstafa();
			 * mod.setCodigoModalidadEstafa(modalidad); obj.setModalidadEstafa(mod);
			 */
	        
	     // OBTENER OBJETOS COMPLETOS DESDE BD
	        MedioEstafa m = medEstServices.obtenerPorId(medio);
	        ModalidadEstafa mod = modEstServices.obtenerPorId(modalidad);

	        obj.setMedioEstafa(m);
	        obj.setModalidadEstafa(mod);
	        
	        //setear por defecto el estado 1 = pendiente
	        EstadoEstafa estado = new EstadoEstafa();
	        estado.setCodigoEstadoEstafa(2);
	        obj.setEstadoEstafa(estado);
	        
	        //ASIGNAR EL USUARIO LOGEADO
	        obj.setUsuario(usuario);
	        
	        estafaServices.registrarEstafa(obj);
	        
	        // --- Enviar correo si marcó checkbox ---
	        if (Boolean.TRUE.equals(formal)) {
	        	
	            // VALIDAR intentos disponibles
	            if (usuario.getContadorIntentos() <= 0) {
	                return ResponseEntity.badRequest()
	                        .body("No tienes intentos disponibles para envío formal.");
	            }
	            
	            String destinatarioDivindat = "percyval_25@outlook.com"; // correo de ejemplo
	            String asunto = "Nueva denuncia formal de delito digital" + " " + titulo;
	            String cuerpoHtml = "<h2>Nuevo caso registrado</h2>"
	                    + "<p><strong>Título:</strong> " + titulo + "</p>"
	                    + "<p><strong>Descripción:</strong> " + descripcion + "</p>"
	                    + "<p><strong>Presunto implicado:</strong> " + ciberdelincuente + "</p>"
	                    + "<p><strong>Modalidad:</strong> " + obj.getModalidadEstafa().getNombreModalidadEstafa() + "</p>"
	                    + "<p><strong>Medio:</strong> " + obj.getMedioEstafa().getNombreMedioEstafa() + "</p>"
	                    + "<p><img src='" + urlImagen + "' width='300'/></p>"
	                    + "<p><strong>Registrado por:</strong> " + usuario.getCorreoUsuario() + " (" + correo + ")</p>";

	            corServices.enviarCorreoDivindat(destinatarioDivindat, asunto, cuerpoHtml);
	            
	            // ✅ RESTAR INTENTO SOLO SI SE ENVÍA A DIVINDAT
	            usuario.setContadorIntentos(usuario.getContadorIntentos() - 1);
	            usuServices.registrarUsuario(usuario);
	        }
	        System.out.println(urlImagen);
	        return ResponseEntity.ok("El registro se realizó correctamente");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Error al registrar la estafa");
	    }
	}
	
	/*
	 * @GetMapping("listado") public String personajes(Model model) {
	 * model.addAttribute("listadoEstafas",
	 * estafaServices.listarEstafasAprobadasOrdenDescUsuariosComunes()); return
	 * "listadoCasos"; }
	 */
	
	@GetMapping("/listado")
	public String listado(Model model, Authentication auth) {

	    boolean esPremium = false;
	    Integer idUsuarioLogeado = null;

	    // Caso 1: auth es null → usuario no logeado
	    if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
	        // Caso 2: usuario logeado → revisar rol
	        esPremium = auth.getAuthorities()
	                .stream()
	                .anyMatch(r -> r.getAuthority().equals("ROLE_Premium") ||
	                        r.getAuthority().equals("ROLE_PremiumPlus"));
	        
	        // 👉 obtener correo (username)
	        String correo = auth.getName();
	     // 👉 buscar usuario en BD
	        Usuario usuario = usuServices.buscarPorCorreoUsuario(correo);

	        if (usuario != null) {
	            idUsuarioLogeado = usuario.getCodigoUsuario(); // o como se llame tu PK
	        }
	    }

	    List<Estafa> listado;

	    if (esPremium) {
	        listado = estafaServices.obtenerCasosAprobados(); // todas
	    } else {
	        listado = estafaServices.obtenerTop3CasosAprobados(); // solo 3
	    }

	    model.addAttribute("listadoEstafas", listado);
	    model.addAttribute("idUsuarioLogeado", idUsuarioLogeado);

	    //listas de los medios y modalidades de delito para el buscador
		model.addAttribute("listadoMedio",medEstServices.listaMedioEstafas());
		model.addAttribute("listadoModalidad",modEstServices.listaModalidadEstafas());
	    
	    return "listadoCasos";
	}

	
	@GetMapping("/estafa/{id}")
	@ResponseBody
	public EstafaDTO obtenerEstafa(@PathVariable Integer id) {
	    Estafa e = estafaServices.buscarPorId(id);
	    return new EstafaDTO(e);
	}
	
	@GetMapping("/motivosRechazo/{id}")
	@ResponseBody
	public List<MotivoRechazoDTO> listarMotivosRechazo(@PathVariable Integer id) {
	    return motRechazoServices.obtenerMotivosPorEstafaIdDTO(id);
	}
	
	@GetMapping("/buscar-casos")
	@ResponseBody
	public List<Estafa> buscarCasos(
	        @RequestParam(required = false) String q,
	        @RequestParam(required = false) String implicado,
	        @RequestParam(required = false) Integer modalidad,
	        @RequestParam(required = false) Integer medio
	) {
	    return estafaServices.buscarCasos(q, implicado, modalidad, medio);
	}
	
	
    @PostMapping("/registrarReporte")
    public ResponseEntity<?> registrarReporte(@RequestBody ReporteDTO req, Principal principal) {

        Usuario usuario = usuServices.buscarPorCorreoUsuario(principal.getName());

    	
        Reporte reporte = reporServices.registrarReporte(req.getIdEstafa(), req.getMotivo());
        Estafa estafa = estafaServices.buscarPorId(req.getIdEstafa());

        // Aumentar contador
        int contador = estafa.getContadorReportes() + 1;
        estafa.setContadorReportes(contador);

        // Cambiar estado según cantidad de reportes
        EstadoEstafa estado = new EstadoEstafa();

        if (contador == 1) {
            estado.setCodigoEstadoEstafa(1); // Reportado
        } else if (contador >= 2) {
            estado.setCodigoEstadoEstafa(3); // Bloqueado
        }

        estafa.setEstadoEstafa(estado);

        // Guardar cambios
        estafaServices.registrarEstafa(estafa);
        
        return ResponseEntity.ok(Map.of(
                "mensaje", "Reporte registrado correctamente",
                "idReporte", reporte.getCodigoReporte()
        ));
    }
    
    @GetMapping("/reporte/por-estafa/{id}")
    public ResponseEntity<?> listarReportesPorEstafa(@PathVariable Integer id) {
        return ResponseEntity.ok(reporServices.obtenerReportesPorEstafa(id));
    }

}
