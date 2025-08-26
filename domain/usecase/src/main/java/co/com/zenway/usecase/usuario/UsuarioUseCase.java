package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private final Long ROL_ADMIN = 1L;
    private final Long ROL_USUARIO = 2L;
    public Mono<Usuario> registrarUsuario(Usuario usuario){



        if (usuario.getRolId() == null) {
            usuario.setRolId(ROL_ADMIN);
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
