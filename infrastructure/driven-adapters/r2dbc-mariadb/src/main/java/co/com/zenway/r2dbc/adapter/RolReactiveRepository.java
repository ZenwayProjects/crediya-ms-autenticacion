package co.com.zenway.r2dbc.adapter;

import co.com.zenway.model.rol.Rol;
import co.com.zenway.r2dbc.entity.RolEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolReactiveRepository extends ReactiveCrudRepository<RolEntity, Short>, ReactiveQueryByExampleExecutor<RolEntity> {

    Mono<Rol> findByNombre(String nombre);

}
