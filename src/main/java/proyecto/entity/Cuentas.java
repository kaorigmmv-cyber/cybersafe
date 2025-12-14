package proyecto.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cuentas")
public class Cuentas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cuenta")
	private Integer codigoCuenta;
	
	@Column(name = "numero_cuenta")
	private String numeroCuenta;
	
	@Column(name = "nombre_titular", length = 800)
	private String nombreTitular;
	
	@Column(name = "fecha_vencimiento")
	private String fechaVencimiento;
	
	@Column(name = "cvv")
	private String cvv;
	
	@Column(name = "monto_disponible")
	private String montoDisponible;
}
