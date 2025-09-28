package co.com.zenway.model.rol.gateways;

import co.com.zenway.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {

    Mono<Rol> buscarRolPorDefecto();
}
