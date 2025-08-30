package co.com.zenway.api;

import co.com.zenway.api.dto.UsuarioRegistroDTO;
import co.com.zenway.api.mapper.UsuarioMapper;
import co.com.zenway.api.utils.ConstantesLogger;
import co.com.zenway.usecase.usuario.UsuarioUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Log4j2
public class UsuarioHandler {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final Validator validator;
    private final GlobalErrorHandler globalErrorHandler;




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
                .doFinally(sig -> log.info(ConstantesLogger.FLUJO_TERMINADO, sig))
                .onErrorResume(globalErrorHandler::handler);
    }


    public Mono<ServerResponse> obtenerEmailPorDocumento(ServerRequest serverRequest) {

        return Mono.just(serverRequest.pathVariable("documento"))
                .doOnSubscribe(info -> log.info("Iniciando consulta de email basado en el documento de identidad"))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El documento no está registrado")))
                .flatMap(usuarioUseCase::obtenerEmailPorDocumento)
                .flatMap(email -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("email", email)))
                .doOnNext(resp -> log.info("Consulta realizada con exito"))
                .doOnError(error -> log.error("Error al buscar el email : {}", error.getMessage()))
                .doFinally(sig -> log.info(ConstantesLogger.FLUJO_TERMINADO, sig))
                .onErrorResume(globalErrorHandler::handler);
    }
}
