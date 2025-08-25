package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> registrarUsuario(Usuario usuario){

        if (usuario.getRolId() == null) {
            usuario.setRolId(1L);
        }

        return usuarioRepository.existsByEmail(usuario.getEmail())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new RuntimeException("Email en uso"));
                    } else {
                        return usuarioRepository.registrarUsuario(usuario);
                    }
                });

    }



}
