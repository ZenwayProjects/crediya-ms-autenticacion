package co.com.zenway.model.usuario.gateways;

import co.com.zenway.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Usuario>registrarUsuario(Usuario usuario);

    Mono<Boolean> existsByEmail(String email);

    Mono<Usuario> findByEmail(String email);

    Mono<Usuario> findById(Long usuarioId);


}
