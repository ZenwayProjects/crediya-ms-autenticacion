package co.com.zenway.api.mapper;

import co.com.zenway.api.dto.UsuarioRegistroDTO;
import co.com.zenway.api.dto.UsuarioResponseDTO;
import co.com.zenway.model.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toResponse(Usuario usuario);

    Usuario toModel(UsuarioRegistroDTO usuarioRegistroDTO);

}
