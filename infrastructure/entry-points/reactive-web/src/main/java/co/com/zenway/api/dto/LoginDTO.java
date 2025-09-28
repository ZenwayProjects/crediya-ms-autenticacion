package co.com.zenway.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "El email no puede estar en blanco")
    @Email(message = "Debe ingresar un email real ")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacia")
    private String password;
}
