package co.com.zenway.usecase.usuario;
import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.PasswordEncoder;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class LoginUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@mail.com");
        usuario.setPassword("hashedPassword");
    }

    @Test
    void login_exitoso() {
        when(usuarioRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(usuario));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        StepVerifier.create(loginUseCase.login("test@mail.com", "plainPassword"))
                .expectNextMatches(u -> u.getEmail().equals("test@mail.com"))
                .verifyComplete();

        verify(usuarioRepository).findByEmail("test@mail.com");
        verify(passwordEncoder).matches("plainPassword", "hashedPassword");
    }

    @Test
    void login_contrasenaIncorrecta() {
        when(usuarioRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(usuario));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        StepVerifier.create(loginUseCase.login("test@mail.com", "wrongPassword"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Credenciales no validas"))
                .verify();

        verify(usuarioRepository).findByEmail("test@mail.com");
        verify(passwordEncoder).matches("wrongPassword", "hashedPassword");
    }

    @Test
    void login_usuarioNoExiste() {
        when(usuarioRepository.findByEmail("notfound@mail.com")).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.login("notfound@mail.com", "anyPassword"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Credenciales no validas"))
                .verify();

        verify(usuarioRepository).findByEmail("notfound@mail.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
