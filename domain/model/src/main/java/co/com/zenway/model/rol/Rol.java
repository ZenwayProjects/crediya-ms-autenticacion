package co.com.zenway.model.rol;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Rol {

    private Long id;
    private String nombre;
    private String descripcion;


}
