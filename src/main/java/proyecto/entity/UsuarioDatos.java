package proyecto.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario_datos")
public class UsuarioDatos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario_datos")
	private Integer codigoUsuarioDatos;
	
	@Column(name = "nombres")
	private String nombresUsuarioDatos;
	
	@Column(name = "apellido_paterno")
	private String apellidoPaternoUsuarioDatos;
	
	@Column(name = "apellido_materno")
	private String apellidoMaternoUsuarioDatos;

	@Column(name = "dni")
	private int dniUsuarioDatos;
	
	@Column(name = "codigo_verificador")
	private int codigoVerificadorUsuarioDatos;
	
	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimientoUsuarioDatos;
	
	@OneToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
}
