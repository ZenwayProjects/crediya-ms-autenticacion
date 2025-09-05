package co.com.zenway.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "jorge")
    private String nombre;

    @Schema(example = "perez")
    private String apellido;

    @Schema(example = "jorgeperez@gmail.com")
    private String email;

    @Schema(example = "1")
    private Short rolId;
}
