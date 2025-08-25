package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> registrarUsuario(Usuario usuario){



        if(usuario.getRolId() == null){
            usuario.setRolId(1L);
        }

        return usuarioRepository.registrarUsuario(usuario);

    }



}
