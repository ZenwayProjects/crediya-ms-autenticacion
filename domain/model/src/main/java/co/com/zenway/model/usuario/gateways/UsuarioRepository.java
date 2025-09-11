package co.com.zenway.model.usuario.gateways;

import co.com.zenway.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UsuarioRepository {
    Mono<Usuario>registrarUsuario(Usuario usuario);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByDocumentoIdentidad(String documentoIdentidad);

    Mono<Usuario> obtenerIdYEmailPorDocumentoIdentidad(String documento);

    Mono<Usuario> findByEmail(String email);

    Flux<Usuario> buscarPorEmails(List<String> email);

    Mono<Usuario> findById(Long usuarioId);


}
