package proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.dao.CategoriaRepository;
import proyecto.dao.NoticiaRepository;
import proyecto.entity.*;
import proyecto.services.NoticiaServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dev/noticias")
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LocalStorageService storageService;
    @Autowired
    private NoticiaServices noticiaService;
    
    private static final String TIPO_CIBERSAFE = "CIBERSAFE";
    private static final String PLANTILLA_CIBERSAFE = "noticiaCibersafe";
    // Asume una página de error que maneja las vistas no encontradas
    private static final String REDIRECT_NOT_FOUND = "redirect:/error404";
    // ==========================================================
    // LISTADO PRINCIPAL
    // ==========================================================
    @GetMapping("/CRUD")
    public String crudNoticias(Model model) {
        model.addAttribute("noticias", noticiaRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "noticiasCRUD";
    }

    // ==========================================================
    // OBTENER NOTICIA POR ID (para editar)
    @GetMapping("/obtener/{id}")
    @ResponseBody
    public Noticia obtenerNoticia(@PathVariable Long id) {
        Noticia noticia = noticiaRepository.findById(id).orElse(null);
        
        if (noticia != null) {
            // **FORZAR CARGA DE LA CATEGORÍA**
            // Si la relación es LAZY, fuerza su carga antes de que termine el método.
            // Si no la necesitas, puedes quitar el condicional si es EAGER.
            if (noticia.getCategoria() != null) {
                noticia.getCategoria().getId(); // Acceder a un campo fuerza la carga
            }
        }

        return noticia; // Spring intenta convertir esta 'Noticia' a JSON
    }

    // ==========================================================
    // GUARDAR (CREAR / EDITAR)
    @PostMapping("/guardar")
    public String guardarNoticia(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("tipoNoticia") String tipoNoticia,
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "autor", required = false) String autor,
            @RequestParam(value = "resumen", required = false) String resumen,
            @RequestParam(value = "contenido", required = false) String contenido,
            @RequestParam(value = "referencias", required = false) String referencias,
            @RequestParam(value = "urlNoticia", required = false) String urlNoticia,
            @RequestParam(value = "fechaPublicacion") LocalDate fechaPublicacion,
            @RequestParam(value = "destacada", defaultValue = "false") boolean destacada,
            @RequestParam("estado") String estado,
            @RequestParam(value = "categoriaSeleccionada", required = true) Long categoriaSeleccionada, // Ahora es obligatorio
            @RequestParam(value = "portada", required = false) MultipartFile portada
    ) {
    	Noticia noticiaExistente = noticiaRepository.findByTitulo(titulo);



        Noticia noticia = (id != null)
                ? noticiaRepository.findById(id).orElse(new Noticia())
                : new Noticia();

        // CAMPOS GENERALES
        noticia.setTitulo(titulo);
        noticia.setAutor(autor);
        noticia.setResumen(resumen);
        noticia.setFechaPublicacion(fechaPublicacion);
        noticia.setDestacada(destacada);
        noticia.setEstado(estado);
        noticia.setTipoNoticia(tipoNoticia);

        // MANEJO SEGÚN TIPO DE NOTICIA
        if (tipoNoticia.equals("CIBERSAFE")) {
            noticia.setContenido(contenido);
            noticia.setReferencias(referencias);
            noticia.setUrlNoticia(null); // No aplica para CIBERSAFE
        } else { // OTROS MEDIOS
            noticia.setContenido(null); // No aplica para OTROS MEDIOS
            noticia.setReferencias(null); // No aplica para OTROS MEDIOS
            noticia.setUrlNoticia(urlNoticia);
        }

        // Asignar categoría
        Categoria categoria = categoriaRepository.findById(categoriaSeleccionada).orElse(null);
        if (categoria != null) {
            noticia.setCategoria(categoria); // Asignar la categoría seleccionada
        } else {
            // Si no se encuentra la categoría, puedes manejarlo como un error o redirigir.
            // Por ejemplo:
            return "redirect:/dev/noticias/CRUD?error=CategoriaNoEncontrada";
        }

        // ==========================================================
        // PORTADA
        // ==========================================================
        if (portada != null && !portada.isEmpty()) {
            String nombre = storageService.saveFile(portada, "portada");
            noticia.setPortadaPath(nombre);
        } else if (id != null) {
            // Si no se sube una nueva portada, mantener la portada existente
            Noticia existingNoticia = noticiaRepository.findById(id).orElse(null);
            if (existingNoticia != null) {
                noticia.setPortadaPath(existingNoticia.getPortadaPath());
            }
        }

        // Guardar la noticia
        noticiaRepository.save(noticia);
        return "redirect:/dev/noticias/CRUD"; // Redirige al listado de noticias
    }

    // ==========================================================
    // ELIMINAR NOTICIA
    // ==========================================================
    @DeleteMapping("/{id}")
    @ResponseBody
    public String eliminarNoticia(@PathVariable Long id) {
        noticiaRepository.deleteById(id);
        return "ok";
    }
    @GetMapping("/ver/{id}")
    public String verNoticiaCibersafe(
            @PathVariable("id") Long id, 
             // Aunque no se usa para buscar, se mantiene por SEO
            Model model,
            RedirectAttributes ra) {
        
        // Usar el servicio para obtener la noticia
        Noticia noticia = noticiaService.obtenerNoticia(id); // Usa tu método existente
      
        List<Noticia> recomendadas = noticiaService.obtenerNoticiasRecomendadas(id);  // Obtener noticias recomendadas
        // 1. Verificar si la noticia existe
        if (noticia == null) {
            ra.addFlashAttribute("error", "Noticia no encontrada.");
            return REDIRECT_NOT_FOUND;
        }

        // 2. Verificar si el tipo de noticia es 'CIBERSAFE'
        if (TIPO_CIBERSAFE.equals(noticia.getTipoNoticia())) {
            
            // Opcional: Validar si el slug del título coincide (buena práctica de SEO/seguridad)
            // String expectedSlug = SlugsUtil.toSlug(noticia.getTitulo()); // Si tienes una utilidad de slugs
            // if (!tituloSlug.equals(expectedSlug)) { ... return REDIRECT_NOT_FOUND }
            
            model.addAttribute("noticia", noticia); 
            model.addAttribute("recomendadas", recomendadas);  // Pasar las noticias recomendadas al modelo

            // Redirige a la plantilla correspondiente
           
            // Redirige al recurso /static/portada/{portadaPath}
            model.addAttribute("rutaPortada", "/static/portada/" + noticia.getPortadaPath()); 
            
            return PLANTILLA_CIBERSAFE;
            
        } else {
            // 3. El tipo no es CIBERSAFE
            ra.addFlashAttribute("aviso", "El recurso solicitado no es una noticia CIBERSAFE.");
            // Aquí puedes redirigir a una vista genérica de noticia si existe, o a un error 404/Home
            return REDIRECT_NOT_FOUND; 
        }
    }
    
    @GetMapping("/noticiasAlertas")
    public String mostrarNoticiasAlertas(Model model) {
    	 List<Noticia> noticias = noticiaRepository.findAll();  // Método que obtiene todas las noticias
         model.addAttribute("noticias", noticias);

        // Retorna el nombre de la vista HTML (Thymeleaf lo resolverá a 'noticiasAlertas.html')
        return "noticiasAlertas";
    }


}
