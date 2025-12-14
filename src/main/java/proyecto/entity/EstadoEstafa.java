package proyecto.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estado_estafa")
public class EstadoEstafa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estado")
	private Integer codigoEstadoEstafa;
		
	@Column(name = "nombre_estado")
	private String nombreEstadoEstafa;
	
	@OneToMany(mappedBy = "estadoEstafa")
	@JsonIgnore
	List<Estafa> listaEstafas;
}
