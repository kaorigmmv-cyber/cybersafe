package proyecto.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotivoRechazoDTO {
    private String motivoRechazo;
    private LocalDate fechaRechazo;
}