package proyecto.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import proyecto.dto.EstafaDTO;
import proyecto.dto.MisCasosRegistradosDTO;
import proyecto.entity.Estafa;
import proyecto.entity.MotivoRechazo;
import proyecto.entity.Rol;
import proyecto.entity.Usuario;
import proyecto.entity.UsuarioDatos;
import proyecto.services.CorreoServices;
import proyecto.services.EstafaServices;
import proyecto.services.MedioEstafaServices;
import proyecto.services.ModalidadEstafaServices;
import proyecto.services.RolServices;
import proyecto.services.UsuarioDatosServices;
import proyecto.services.UsuarioServices;
import proyecto.services.VerificacionTokenServices;

@Controller
@RequestMapping("user")
public class UsuarioController {

    private final PasswordEncoder passwordEncoder;

	@Autowired
	private CorreoServices correoServices;
	
	@Autowired
	private UsuarioDatosServices usuDatosServices;
	
	@Autowired
	private UsuarioServices usuServices;
	
	@Autowired
	private VerificacionTokenServices veriTokenServices;
	
	@Autowired
	private RolServices rolServices;

	@Autowired
	private EstafaServices estServices;
	
	@Autowired
	private ModalidadEstafaServices modEstServices;

	@Autowired
	private MedioEstafaServices medEstServices;
	
    UsuarioController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
	@GetMapping("/correoEnviado")
	public String correoEnviado(@RequestParam String correo, Model model) {
	    model.addAttribute("correo", correo);
	    return "correoEnviadoRegistroUsuario"; // tu vista Thymeleaf
	}
	
	//validacion de dni y correo
	@GetMapping("/validarCorreo")
	@ResponseBody
	public boolean validarCorreo(@RequestParam("correo") String correo) {
	    return !usuServices.correoExiste(correo); // true = disponible, false = ya existe
	}

	@GetMapping("/validarDni")
	@ResponseBody
	public boolean validarDni(@RequestParam("dni") int dni) {
	    return !usuDatosServices.dniExiste(dni); // true = disponible
	}

	
	@GetMapping("registrarse")
	public String registrarse() {
	    return "registrarse";
	}
	
	@PostMapping("/registrar")
	public String registroPruebo(
	        @RequestParam("nombres") String nombre,
	        @RequestParam("apellidoPaterno") String apellidoPaterno,
	        @RequestParam("apellidoMaterno") String apellidoMaterno,
	        @RequestParam("dni") int dni,
	        @RequestParam("digitoVerificador") int codigoVerificador,
	        @RequestParam("correoElectronico") String correoElectronico,
	        @RequestParam("password") String password,
	        @RequestParam("fechaNacimiento") LocalDate fechaNacimiento,
	        @RequestParam("nickname") String nickname,
	        Model model) {

	    try {

	        // 1. Crear el USUARIO
	        Usuario usuario = new Usuario();
	        usuario.setCorreoUsuario(correoElectronico);
	        usuario.setPasswordUsuario(passwordEncoder.encode(password));
	        usuario.setNicknameUsuario(nickname);
	        usuario.setActivadoUsuario(false);
	        usuario.setFechaCreacionUsuario(LocalDateTime.now());

	        // Guardar usuario (YA TIENE ID)
	        Usuario usuarioGuardado = usuServices.registrarUsuario(usuario);


	        // 2. Crear los DATOS DEL USUARIO
	        UsuarioDatos usuarioDatos = new UsuarioDatos();
	        usuarioDatos.setNombresUsuarioDatos(nombre);
	        usuarioDatos.setApellidoPaternoUsuarioDatos(apellidoPaterno);
	        usuarioDatos.setApellidoMaternoUsuarioDatos(apellidoMaterno);
	        usuarioDatos.setFechaNacimientoUsuarioDatos(fechaNacimiento);
	        usuarioDatos.setDniUsuarioDatos(dni);
	        usuarioDatos.setCodigoVerificadorUsuarioDatos(codigoVerificador);

	        // ⭐ RELACIÓN 1 A 1
	        usuarioDatos.setUsuario(usuarioGuardado);

	        // Guardar datos del usuario
	        usuDatosServices.registrarUsuarioDatos(usuarioDatos);

	     // después de guardar el usuario
	        Rol rolUsuario = rolServices.obtenerRolPorNombre("Usuario");
	        System.out.println("ROL ENCONTRADO = " + rolUsuario);

	        usuarioGuardado.getRoles().add(rolUsuario);

	        // guardar nuevamente para persistir la relación
	        usuServices.registrarUsuario(usuarioGuardado);
	        	        
	        // 3. GENERAR TOKEN
	        String token = veriTokenServices.crearTokenParaUsuario(usuarioGuardado);

	        // 4. ENVIAR CORREO
	        correoServices.enviarCorreoConfirmacion(correoElectronico, token);

	        model.addAttribute("correo", correoElectronico);
	        return "redirect:/user/correoEnviado?correo=" + correoElectronico;
	    } catch (Exception e) {
	        e.printStackTrace(); // se muestra en consola

	        model.addAttribute("mensaje", "No se pudo completar el registro. Intente nuevamente.");
	        return "errorGeneral";
	    }
	}
	
	@GetMapping("/confirmar")
	public String confirmarCuenta(
	        @RequestParam("token") String token,
	        Model model
	) {

	    boolean activado = veriTokenServices.activarCuenta(token);

	    if (activado) {
	        model.addAttribute("mensaje", "Tu cuenta ha sido activada correctamente.");
	        return "correoCuentaConfirmada"; // VISTA QUE DEBES CREAR
	    } else {
	        model.addAttribute("mensaje", "El enlace es inválido o ha expirado.");
	        return "errorGeneral";
	    }
	}
	
	@GetMapping("misCasosRegistrados")
	public String misCasosRegistrados(Model model) {
		model.addAttribute("listadoMedio",medEstServices.listaMedioEstafas());
		model.addAttribute("listadoModalidad",modEstServices.listaModalidadEstafas());
	    return "misCasosRegistrados";
	}
	
    @GetMapping("/listarMisCasos")
    @ResponseBody
    public List<MisCasosRegistradosDTO> obtenerMisCasos(Principal principal) {

        String email = principal.getName();
        Usuario usuario = usuServices.buscarPorCorreoUsuario(email);

        List<Estafa> lista = estServices.listarPorUsuario(usuario.getCodigoUsuario());

        return lista.stream()
                    .map(MisCasosRegistradosDTO::new)
                    .collect(Collectors.toList());
    }

}
