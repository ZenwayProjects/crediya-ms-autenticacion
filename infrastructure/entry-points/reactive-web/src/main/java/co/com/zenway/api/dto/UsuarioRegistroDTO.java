package co.com.zenway.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UsuarioRegistroDTO {


    @Schema(example = "jorge")
    @NotBlank
    private String nombre;

    @Schema(example = "perez")
    @NotBlank
    private String apellido;

    @Schema(example = "jorgeperez@gmail.com")
    @NotBlank
    @Email(message = "El formato no es válido")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
    private String email;

    private LocalDate fechaNacimiento;

    @Schema(example = "765738929")
    @NotBlank
    private String documentoIdentidad;

    @Schema(example = "313267458")
    @NotNull
    private String telefono;

    @Schema(example = "1800000")
    @Min(value = 0, message = "El salario no puede ser negativo")
    @Max(value = 15000001, message = "El salario no debe exceder los 15 millones")
    private BigDecimal salarioBase;


}
