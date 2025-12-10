package proyecto.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "verification_token")
public class VerificacionToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_verificacion_token")
	private Integer codigoVerificacionToken;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "fecha_expiracion")
	private LocalDateTime fechaExpiracionToken;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
}
