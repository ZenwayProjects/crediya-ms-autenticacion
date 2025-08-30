package co.com.zenway.r2dbc.adapter;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import co.com.zenway.r2dbc.entity.UsuarioEntity;
import co.com.zenway.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;


@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario/* change for domain model */,
        UsuarioEntity/* change for adapter model */,
    Long,
        UsuarioReactiveRepository
> implements UsuarioRepository {
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

        return this.save(usuario).as(transactionalOperator::transactional);
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
    public Mono<String> obtenerEmailPorDocumentoIdentidad(String documento) {
        return repository.findEmailByDocumento(documento);
    }

    @Override
    public Mono<Usuario> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email);
    }
}
