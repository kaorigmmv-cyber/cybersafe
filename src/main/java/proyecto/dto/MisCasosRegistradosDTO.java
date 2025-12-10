package proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto.entity.Estafa;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MisCasosRegistradosDTO {
	
	private Integer id;
	private String titulo;
	private String descripcion;
	private String modalidad;
	private String medio;
	private String imagen;
	private String implicado;
	private int idUsuario;
	private String estadoEstafa;
	
	public MisCasosRegistradosDTO(Estafa e) {
		this.id = e.getCodigoEstafa();
		this.titulo = e.getTituloCaso();
		this.descripcion = e.getDescripcionEstafa();
		this.imagen = e.getImagenEstafa();
		this.implicado = e.getCiberdelincuente();
		this.modalidad = e.getModalidadEstafa().getNombreModalidadEstafa();
		this.medio = e.getMedioEstafa().getNombreMedioEstafa();
		this.idUsuario = e.getUsuario().getCodigoUsuario();
		this.estadoEstafa = e.getEstadoEstafa().getNombreEstadoEstafa();
	}
}
