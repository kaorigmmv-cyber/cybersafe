package proyecto.entity;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estafa")
public class Estafa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estafa")
	private Integer codigoEstafa;
	
	@Column(name = "titulo_caso")
	private String tituloCaso;
	
	@Column(name = "descripcion_estafa", length = 800)
	private String descripcionEstafa;
	
	@Column(name = "imagen_estafa")
	private String imagenEstafa;

	@Column(name = "ciberdelincuente")
	private String ciberdelincuente;
	
	@Column(name = "fecha_reporte")
	private LocalDate fechaReporte;
	
	@Column(name = "contador_reportes")
	private Integer contadorReportes;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="id_medio_estafa")
	private MedioEstafa medioEstafa;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="id_modalidad_estafa")
	private ModalidadEstafa modalidadEstafa;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="id_estado")
	private EstadoEstafa estadoEstafa;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	@OneToMany(mappedBy = "estafa")
	@JsonIgnore
	List<MotivoRechazo> listaMotivoRechazo;
	
	@OneToMany(mappedBy = "estafa")
	@JsonIgnore
	List<Reporte> listaReportes;
	
}
