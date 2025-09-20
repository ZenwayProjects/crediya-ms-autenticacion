package co.com.zenway.r2dbc.adapter;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Long>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<Boolean> existsByEmailIgnoreCase(String email);
    Mono<Usuario> findByEmailIgnoreCase(String email);

    Mono<Boolean> existsByDocumentoIdentidadIgnoreCase(String email);

    Mono<Usuario> findByDocumentoIdentidad(String documento);

    Flux<UsuarioEntity> findByEmailIn(List<String> emails);
    


}
