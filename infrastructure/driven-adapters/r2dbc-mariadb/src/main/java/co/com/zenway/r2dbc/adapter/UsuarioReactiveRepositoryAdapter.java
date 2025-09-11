package co.com.zenway.r2dbc.adapter;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import co.com.zenway.r2dbc.entity.UsuarioEntity;
import co.com.zenway.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.log4j.Log4j2;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
@Log4j2
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario/* change for domain model */,
        UsuarioEntity/* change for adapter model */,
        Long,
        UsuarioReactiveRepository> implements UsuarioRepository {

            private final TransactionalOperator transactionalOperator;

    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Usuario.class/* change for domain model */));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Usuario> registrarUsuario(Usuario usuario) {
        log.info("Guardando usuario en DB: {}", usuario);
        return this.save(usuario)
                .doOnSuccess(saved -> log.info("Usuario guardado: {}", saved))
                .doOnError(e -> log.error("Error al guardar usuario", e))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public Mono<Boolean> existsByDocumentoIdentidad(String documentoIdentidad) {
        return repository.existsByDocumentoIdentidadIgnoreCase(documentoIdentidad);
    }

    @Override
    public Mono<Usuario> obtenerIdYEmailPorDocumentoIdentidad(String documento) {
        return repository.findIdAndEmailByDocumento(documento);
    }

    @Override
    public Mono<Usuario> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email);
    }

    @Override
    public Flux<Usuario> buscarPorEmails(List<String> email) {
        return repository.findByEmailIn(email)
                .map(entity -> mapper.map(entity, Usuario.class));
    }
}
