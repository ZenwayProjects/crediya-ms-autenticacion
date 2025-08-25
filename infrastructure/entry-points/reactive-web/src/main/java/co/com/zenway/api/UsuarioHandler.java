package co.com.zenway.api;

import co.com.zenway.api.dto.UsuarioRegistroDTO;
import co.com.zenway.api.mapper.UsuarioMapper;
import co.com.zenway.usecase.usuario.UsuarioUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
@Log4j2
public class UsuarioHandler {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final Validator validator;
    private final GlobalErrorHandler globalErrorHandler;




    @Transactional
    public Mono<ServerResponse> registrarUsuario(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(UsuarioRegistroDTO.class)
                .doOnSubscribe(i -> log.info("Iniciando registro de usuario"))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Body requerido")))
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty()) return Mono.error(new ConstraintViolationException(violations));
                    return Mono.just(dto);
                })
                .map(usuarioMapper::toModel)
                .flatMap(usuarioUseCase::registrarUsuario)
                .map(usuarioMapper::toResponse)
                .flatMap(resp -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(resp)
                )
                .doOnNext(resp -> log.info("Usuario registrado: {}", resp))
                .doOnError(e -> log.error("Error al registrar: {}", e.getMessage(), e))
                .doFinally(sig -> log.info("Flujo terminado: {}", sig))
                .onErrorResume(globalErrorHandler::handler);
    }


    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue(serverRequest);
    }
}
