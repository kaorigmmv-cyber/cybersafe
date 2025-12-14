package proyecto.entity;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

import java.util.UUID;

@Service
public class LocalStorageService {

    // Carpeta raíz donde se guardarán los archivos (fuera de /static)
    private final Path root = Paths.get("uploads");

    public LocalStorageService() {
        try {
            Files.createDirectories(root.resolve("portada"));
            Files.createDirectories(root.resolve("secundarias"));
            Files.createDirectories(root.resolve("galeria"));
        } catch (IOException e) {
            throw new RuntimeException("Error creando carpetas para subir archivos", e);
        }
    }

    /**
     * Guarda un archivo en una subcarpeta (portada, secundarias, galeria)
     * @param file archivo subido
     * @param folder carpeta destino
     * @return URL pública accesible
     */
    public String saveFile(MultipartFile file, String folder) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            String originalName = file.getOriginalFilename();
            String filename = UUID.randomUUID() + "_" + originalName;

            Path destinationFolder = root.resolve(folder);
            Path destinationFile = destinationFolder.resolve(filename);

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // Esta URL será accesible por el usuario (gracias al ResourceHandler)
            return "/uploads/" + folder + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo en " + folder, e);
        }
    }

    /**
     * Elimina un archivo físico si se desea
     */
    public void deleteFile(String urlPath) {
        try {
            if (urlPath == null) return;

            // urlPath viene así: /uploads/portada/archivo.jpg
            String relativePath = urlPath.replace("/uploads/", "");

            Path filePath = root.resolve(relativePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar archivo: " + urlPath, e);
        }
    }
}
