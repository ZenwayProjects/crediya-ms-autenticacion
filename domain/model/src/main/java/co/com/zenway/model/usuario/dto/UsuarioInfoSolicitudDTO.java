package co.com.zenway.model.usuario.dto;

import java.math.BigDecimal;

public record UsuarioInfoSolicitudDTO(Long idUsuario,String nombre, String email, BigDecimal salarioBase)
{
}
