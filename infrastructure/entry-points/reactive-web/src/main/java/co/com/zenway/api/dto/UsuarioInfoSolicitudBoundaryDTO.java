package co.com.zenway.api.dto;

import co.com.zenway.model.usuario.dto.UsuarioInfoSolicitudDTO;

import java.math.BigDecimal;

public record UsuarioInfoSolicitudBoundaryDTO(Long id, String nombre, String email, BigDecimal salarioBase) {


        public UsuarioInfoSolicitudBoundaryDTO(UsuarioInfoSolicitudDTO usuario){
                this(usuario.idUsuario(), usuario.nombre(), usuario.email(), usuario.salarioBase());
        }
}
