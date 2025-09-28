package co.com.zenway.api;

import co.com.zenway.api.dto.*;
import co.com.zenway.api.exceptions.GlobalErrorHandler;
import co.com.zenway.api.mapper.UsuarioMapper;
import co.com.zenway.api.utils.ConstantesLogger;
import co.com.zenway.security.adapter.JwtProvider;
import co.com.zenway.usecase.usuario.LoginUseCase;
import co.com.zenway.usecase.usuario.RegistroUsuarioUseCase;
import co.com.zenway.usecase.usuario.SolicitudUseCase;
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

import static co.com.zenway.api.utils.ConstantesErrores.BODY_REQUERIDO;


@Component
@RequiredArgsConstructor
@Log4j2
public class UsuarioHandler {

    private final RegistroUsuarioUseCase registroUsuarioUseCase;
    private final SolicitudUseCase solicitudUseCase;

    private final LoginUseCase loginUseCase;
    private final UsuarioMapper usuarioMapper;
    private final Validator validator;
    private final GlobalErrorHandler globalErrorHandler;
    private final JwtProvider jwtProvider;


    public Mono<ServerResponse> login(ServerRequest serverRequest){
        return serverRequest.bodyToMono(LoginDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(BODY_REQUERIDO)))
                .flatMap(loginDTO -> loginUseCase.login(loginDTO.getEmail(), loginDTO.getPassword()))
                .map(jwtProvider::generarToken)
                .map(TokenResponseDTO::new)
                .flatMap(resp -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(resp)
                )
                .doOnNext(resp -> log.info("Usuario logueado: {}", resp))
                .doOnError(e -> {
                    log.error("Error en login: {}", e.getMessage(), e);
                    Throwable cause = e.getCause();
                    while (cause != null) {
                        log.error("Caused by: {}", cause.getMessage(), cause);
                        cause = cause.getCause();
                    }
                })
                .doFinally(sig -> log.info(ConstantesLogger.FLUJO_TERMINADO, sig))
                .onErrorResume(globalErrorHandler::handler);
    }


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
                .flatMap(registroUsuarioUseCase::registrarUsuario)
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
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El documento no puede estar vacío")))
                .flatMap(solicitudUseCase::obtenerUsuarioIdYEmailPorDocumento)
                .flatMap(usuarioInfoSolicitudDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioInfoSolicitudBoundaryDTO(usuarioInfoSolicitudDTO))
                        .doOnNext(resp -> log.info("Datos del usuario :{} :{}", usuarioInfoSolicitudDTO.idUsuario(), usuarioInfoSolicitudDTO.email()))
                )
                .doOnNext(resp -> log.info("Consulta realizada con exito"))
                .doOnError(error -> log.error("Error al buscar el id y el email : {}", error.getMessage()))
                .doFinally(sig -> log.info(ConstantesLogger.FLUJO_TERMINADO, sig))
                .onErrorResume(globalErrorHandler::handler);
    }


    public Mono<ServerResponse> obtenerUsuariosPorEmail(ServerRequest serverRequest){
        return serverRequest.bodyToMono(UsuariosPorEmailsRequestDTO.class)
                .flatMapMany(lista -> solicitudUseCase.obtenerUsuariosPorEmails(lista.emails()))
                .collectList()
                .flatMap(usuarios -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(usuarios));
    }

}
