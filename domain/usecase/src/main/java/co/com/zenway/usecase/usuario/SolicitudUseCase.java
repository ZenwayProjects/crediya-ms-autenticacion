package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.dto.UsuarioInfoSolicitudDTO;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import co.com.zenway.usecase.usuario.exception.DocumentoNoExiste;
import co.com.zenway.usecase.usuario.utils.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<UsuarioInfoSolicitudDTO> obtenerUsuarioIdYEmailPorDocumento(String documento) {
        return usuarioRepository.existsByDocumentoIdentidad(documento.trim())
                .flatMap(existsDocument -> {
                    if (Boolean.FALSE.equals(existsDocument)){
                        return Mono.error(new DocumentoNoExiste(Constantes.DOCUMENTO_IDENTIDAD_NO_REGISTRADO));
                    }
                    return usuarioRepository.obtenerIdYEmailPorDocumentoIdentidad(documento)
                            .doOnNext(model -> System.out.println("Model -> " + model.getId()))
                            .map(model -> new UsuarioInfoSolicitudDTO(model.getId(), model.getEmail()));
                });
    }

    public Flux<Usuario> obtenerUsuariosPorEmails(List<String> emails){
        return usuarioRepository.buscarPorEmails(emails);
    }
}
