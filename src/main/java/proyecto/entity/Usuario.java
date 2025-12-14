package proyecto.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer codigoUsuario;
	
	@Column(name = "email_usuario")
	private String correoUsuario;
	
	@Column(name = "pass_usuario")
	private String passwordUsuario;
	
	@Column(name = "nickname")
	private String nicknameUsuario;

	@Column(name = "activado")
	private boolean activadoUsuario;
	
	@Column(name = "contador_intentos")
	private Integer contadorIntentos ;
	
	@Column(name = "fecha_creacion_usuario")
	private LocalDateTime fechaCreacionUsuario;
	
	@OneToOne(mappedBy = "usuario")
	private UsuarioDatos usuarioDatos;
	
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	List<VerificacionToken> listaToken;
	
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	List<Estafa> listaEstafas;
	
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	List<MotivoRechazo> listaMotivoRechazo;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_rol",
	    joinColumns = @JoinColumn(name = "id_usuario"),
	    inverseJoinColumns = @JoinColumn(name = "id_rol")
	)
	private List<Rol> roles = new ArrayList<>();
	
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	List<Reporte> listaReportes;
}
