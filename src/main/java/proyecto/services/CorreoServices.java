package proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class CorreoServices {
	
    @Autowired
    private JavaMailSender mailSender;
    
    public void enviarCorreoConfirmacion(String destino, String token) {

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Confirmación de Cuenta");
        mensaje.setText("Haz clic en el siguiente enlace para confirmar tu cuenta: "
                + "https://cybersafe-xmnh.onrender.com/user/confirmar?token=" + token);

        mailSender.send(mensaje);
        System.out.println("Correo enviado a " + destino);
    }
    
    public void enviarCorreoDivindat(String destinatario, String asunto, String cuerpoHtml) {
        try {
        	//CORREO MAS ELABORADO, QUE PERMITE ENVIO DE IMAGENES
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
