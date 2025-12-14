package proyecto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoPremiumDTO {

    private String numeroCuenta;
    private String nombreTitular;
    private String fechaVencimiento;
    private String cvv;
}
