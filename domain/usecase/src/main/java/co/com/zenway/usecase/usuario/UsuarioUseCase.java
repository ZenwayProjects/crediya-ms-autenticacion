package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import co.com.zenway.usecase.usuario.exceptions.DocumentoDeIdentidadEnUso;
import co.com.zenway.usecase.usuario.exceptions.EmailEnUso;
import co.com.zenway.usecase.usuario.utils.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private static final Long ROL_ADMIN = 1L;
    private static final Long ROL_USUARIO = 2L;
    public Mono<Usuario> registrarUsuario(Usuario usuario) {


        if (usuario.getRolId() == null) {
            usuario.setRolId(ROL_ADMIN);
        }

        return usuarioRepository.existsByEmail(usuario.getEmail())
                .flatMap(existsEmail -> {
                    if (Boolean.TRUE.equals(existsEmail)) {
                        return Mono.error(new EmailEnUso(Constantes.EMAIL_EN_USO));
                    }
                    return usuarioRepository.existsByDocumentoIdentidad(usuario.getDocumentoIdentidad().trim());
                })
                .flatMap(existsDoc -> {
                    if (Boolean.TRUE.equals(existsDoc)) {
                        return Mono.error(new DocumentoDeIdentidadEnUso(Constantes.DOCUMENTO_IDENTIDAD_EN_USO));
                    }
                    return usuarioRepository.registrarUsuario(usuario);
                });
    }

}
