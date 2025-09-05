package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.PasswordEncoder;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public Mono<Usuario> login(String email, String password){

        return usuarioRepository.findByEmail(email)
                .filter(usuario -> passwordEncoder.matches(password, usuario.getPassword()))
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales no validas")));
    }



}
