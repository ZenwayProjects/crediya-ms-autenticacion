package co.com.zenway.usecase.usuario;

import co.com.zenway.model.rol.gateways.RolRepository;
import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.dto.UsuarioInfoSolicitudDTO;
import co.com.zenway.model.usuario.gateways.PasswordEncoder;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import co.com.zenway.usecase.usuario.exception.DocumentoDeIdentidadEnUso;
import co.com.zenway.usecase.usuario.exception.DocumentoNoExiste;
import co.com.zenway.usecase.usuario.exception.EmailEnUso;
import co.com.zenway.usecase.usuario.utils.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistroUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<Usuario> registrarUsuario(Usuario usuario) {

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
                    Mono<Usuario> usuarioConRol = usuario.getRolId() == null
                            ? asignarRolPorDefecto(usuario)
                            : Mono.just(usuario);

                    return usuarioConRol
                            .flatMap(this::encriptarPassword)
                            .flatMap(usuarioRepository::registrarUsuario);
                });
    }



    private Mono<Usuario> asignarRolPorDefecto(Usuario usuario){
        return rolRepository.buscarRolPorDefecto()
                .map(rol -> {
                    usuario.setRolId(rol.getId());
                    return usuario;
                });
    }

    private Mono<Usuario> encriptarPassword(Usuario usuario){
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);
        return Mono.just(usuario);
    }

}
