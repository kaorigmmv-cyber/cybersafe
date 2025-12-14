package proyecto.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "reporte")
public class Reporte {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_codigo_reporte")
	private Integer codigoReporte;
		
	@Column(name = "motivo")
	private String motivoReporte;
	
	@Column(name = "fecha_reporte")
	private LocalDateTime fechaReporte;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="id_usuario_reporta")
	private Usuario usuario;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="id_estafa")
	private Estafa estafa;
}
