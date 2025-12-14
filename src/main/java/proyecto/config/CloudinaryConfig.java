package proyecto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dpsypxqzp",
                "api_key", "843569795737733",
                "api_secret", "xO06RVj0Jb8q3PzNwT5vz5wPTNs",
                "secure", true
        ));
    }
}
