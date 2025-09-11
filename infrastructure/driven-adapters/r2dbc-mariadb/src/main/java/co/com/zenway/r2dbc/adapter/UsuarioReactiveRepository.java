package co.com.zenway.r2dbc.adapter;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.dto.UsuarioInfoSolicitudDTO;
import co.com.zenway.r2dbc.entity.UsuarioEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Long>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<Boolean> existsByEmailIgnoreCase(String email);
    Mono<Usuario> findByEmailIgnoreCase(String email);

    Mono<Boolean> existsByDocumentoIdentidadIgnoreCase(String email);

    @Query("SELECT id_usuario, email FROM usuario WHERE documento_identidad = :documento LIMIT 1")
    Mono<Usuario> findIdAndEmailByDocumento(String documento);

    Flux<UsuarioEntity> findByEmailIn(List<String> emails);
    


}
