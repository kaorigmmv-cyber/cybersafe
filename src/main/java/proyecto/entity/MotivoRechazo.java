package proyecto.entity;

import java.time.LocalDate;

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
@Table(name = "MotivoRechazo")
public class MotivoRechazo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer codigoRechazo;
	
	@Column(name = "motivo")
	private String motivoRechazo;
	
	@Column(name = "fechaRechazo")
	private LocalDate fechaRechazo;
	
	@ManyToOne
	@JoinColumn(name="moderador_id")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="id_estafa")
	private Estafa estafa;
}
