package proyecto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import proyecto.services.UsuarioDetailsService;

@Configuration
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // opcional, dependiendo si manejas API o no
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/user/registrarse",
                    "/",
                    "/user/validarDni",
                    "/user/validarCorreo",
                    "/user/registrar",
                    "/user/confirmar",
                    "/user/correoEnviado",
                    "/dev/estadisticas",
                    "/dev/guias",
                    "/dev/index",
                    "/dev/informacionTipos",
                    "/dev/noticias/**",          
                    "/dev/tiposEstafa",
                    "/dev/guiasDenuncia",
                    "/est/listado",
                    "/est/estafa/**",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/images/**",
                    "/uploads/**"
                ).permitAll()
                .requestMatchers(
                		"/mod/**"   			
                ).hasRole("Moderador")
                .requestMatchers(
                        "/est/registrarCiberdelito",
                        "/est/registrar",
                        "/est/registrarReporte",
                        "/est/reporte/**"                     
                ).hasAnyRole("Usuario", "Premium")
                .anyRequest().authenticated()
            ).exceptionHandling(ex -> ex
                .accessDeniedPage("/dev/error/403")
            	).formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true) // URL a donde SIEMPRE redireccionará después del login
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    // Optional: autenticación manual si necesitas AuthenticationManager
    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration();
    }
}
