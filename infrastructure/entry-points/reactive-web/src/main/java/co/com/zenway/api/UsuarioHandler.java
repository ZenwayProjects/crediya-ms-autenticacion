package co.com.zenway.api;

import co.com.zenway.api.dto.UsuarioRegistroDTO;
import co.com.zenway.api.mapper.UsuarioMapper;
import co.com.zenway.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UsuarioHandler {

    private final UsuarioUseCase usuarioUseCase;
    private final RequestValidator requestValidator;
    private final UsuarioMapper usuarioMapper;

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue(serverRequest);
    }



    @Transactional
    public Mono<ServerResponse> registrarUsuario(ServerRequest serverRequest){

        return serverRequest.bodyToMono(UsuarioRegistroDTO.class)
                .flatMap(requestValidator::validarUsuario)
                .map(usuarioMapper::toModel)
                .flatMap(usuarioUseCase::registrarUsuario)
                .flatMap(usuarioRegistrado -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(usuarioMapper.toResponse(usuarioRegistrado)))
                .onErrorResume(e -> ServerResponse.badRequest().
                        contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage()))
                );
    }
}
