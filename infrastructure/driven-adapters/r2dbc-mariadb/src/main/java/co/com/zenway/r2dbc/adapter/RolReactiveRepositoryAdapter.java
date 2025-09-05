package co.com.zenway.r2dbc.adapter;

import co.com.zenway.model.rol.Rol;
import co.com.zenway.model.rol.gateways.RolRepository;
import co.com.zenway.r2dbc.entity.RolEntity;
import co.com.zenway.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        Short,
        RolReactiveRepository> implements RolRepository {


    protected RolReactiveRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Rol.class) );
    }

    @Override
    public Mono<Rol> buscarRolPorDefecto() {
        return repository.findByNombre("ROLE_USUARIO");
    }
}
