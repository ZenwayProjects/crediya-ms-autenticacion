package co.com.zenway.api.dto;

import co.com.zenway.model.usuario.dto.UsuarioInfoSolicitudDTO;

public record UsuarioInfoSolicitudBoundaryDTO(Long id, String email) {


        public UsuarioInfoSolicitudBoundaryDTO(UsuarioInfoSolicitudDTO usuario){
                this(usuario.idUsuario(), usuario.email());
        }
}
