package proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Mostrar página de login personalizada
    @GetMapping("/login")
    public String login() {
        return "login"; // tu vista Thymeleaf login.html
    }

    // Puedes agregar redirección post-login según rol
    @GetMapping("/home")
    public String homeRedirect() {
        // Spring Security inyecta el usuario autenticado
        // Redirigir según rol
        return "redirect:/dashboard"; 
    }
}
