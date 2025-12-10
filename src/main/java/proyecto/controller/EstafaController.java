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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import proyecto.dto.EstafaDTO;
import proyecto.dto.MotivoRechazoDTO;
import proyecto.entity.EstadoEstafa;
import proyecto.entity.Estafa;
import proyecto.entity.MedioEstafa;
import proyecto.entity.ModalidadEstafa;
import proyecto.entity.MotivoRechazo;
import proyecto.entity.Usuario;
import proyecto.services.CorreoServices;
import proyecto.services.EstafaServices;
import proyecto.services.MedioEstafaServices;
import proyecto.services.ModalidadEstafaServices;
import proyecto.services.MotivoRechazoServices;
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
	
	@GetMapping("registrarCiberdelito")
	public String registrarCiberdelito(Model model) {
		model.addAttribute("listadoMedio",medEstServices.listaMedioEstafas());
		model.addAttribute("listadoModalidad",modEstServices.listaModalidadEstafas());
	    return "registrarCiberdelito";
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
	        
	    	//SUBIR IMAGEN
	        String apiKey = "3d28d03bd2f3ba67b9febc7928ff0c6a";

	        // Convertir imagen a Base64
	        String imgBase64 = Base64.getEncoder().encodeToString(archivo.getBytes());

	        // URL de ImgBB
	        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

	        RestTemplate rest = new RestTemplate();

	        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	        body.add("image", imgBase64);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(body, headers);

	        String respuesta = rest.postForObject(url, req, String.class);

	        ObjectMapper mapper = new ObjectMapper();
	        Map<String, Object> jsonMap = mapper.readValue(respuesta, Map.class);

	        Map<String, Object> data = (Map<String, Object>) jsonMap.get("data");
	        
	     // ---- Obtener URL final de la imagen ----
	        System.out.println("Respuesta completa ImgBB: " + respuesta);

	        String urlImagen = null;

	        // 1. display_url (la más común)
	        if (data.get("display_url") != null) {
	            urlImagen = data.get("display_url").toString();
	        }

	        // 2. url simple
	        if (urlImagen == null && data.get("url") != null) {
	            urlImagen = data.get("url").toString();
	        }

	        // 3. image.url dentro del objeto image
	        if (urlImagen == null && data.get("image") != null) {
	            Map<String, Object> imageData = (Map<String, Object>) data.get("image");
	            if (imageData.get("url") != null) {
	                urlImagen = imageData.get("url").toString();
	            }
	        }

	        // 4. Validación final
	        if (urlImagen == null) {
	            throw new RuntimeException("ImgBB no devolvió ninguna URL válida");
	        }

	        System.out.println("URL final de la imagen: " + urlImagen);

	        //GUARDAMOS REGISTRO
	        Estafa obj = new Estafa();
	        obj.setTituloCaso(titulo);
	        obj.setDescripcionEstafa(descripcion);
	        obj.setImagenEstafa(urlImagen);
	        obj.setCiberdelincuente(ciberdelincuente);
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
	        estado.setCodigoEstadoEstafa(1);
	        obj.setEstadoEstafa(estado);
	        
	        //ASIGNAR EL USUARIO LOGEADO
	        obj.setUsuario(usuario);
	        
	        estafaServices.registrarEstafa(obj);
	        
	        // --- Enviar correo si marcó checkbox ---
	        if (Boolean.TRUE.equals(formal)) {
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
	        }
	        System.out.println(urlImagen);
	        return ResponseEntity.ok("El registro se realizó correctamente");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Error al registrar la estafa");
	    }
	}
	
	@GetMapping("listado")
	public String personajes(Model model) {
	    model.addAttribute("listadoEstafas", estafaServices.listarEstafasAprobadas());
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
}
