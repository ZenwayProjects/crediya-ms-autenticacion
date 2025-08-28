package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioTest;

    private UsuarioUseCase usuarioUseCase;



    @BeforeEach
    void setup() {
        usuarioUseCase = new UsuarioUseCase(usuarioRepository);

        usuarioTest = Usuario.builder()
                .id(150L)
                .nombre("German")
                .apellido("Perez")
                .email("germanperez@gmail.com")
                .documentoIdentidad("193204829")
                .telefono("3163278354")
                .rolId(2L)
                .salarioBase( new BigDecimal("100000"))
                .build();
    }

    @Test
    void shouldRegistrarUsuarioWhenEmailIsNotUsed(){
        when(usuarioRepository.existsByEmail(usuarioTest.getEmail())).thenReturn(Mono.just(false));
        when(usuarioRepository.registrarUsuario(usuarioTest)).thenReturn(Mono.just(usuarioTest));
        when(usuarioRepository.existsByDocumentoIdentidad(usuarioTest.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(usuarioUseCase.registrarUsuario(usuarioTest))
                .expectNext(usuarioTest)
                .verifyComplete();

    }

    @Test
    void shouldAssignRolAdminWhenRolIsNull() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setDocumentoIdentidad("12345232");
        usuario.setRolId(null);

        when(usuarioRepository.existsByEmail(usuario.getEmail()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existsByDocumentoIdentidad(usuario.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.registrarUsuario(any()))
                .thenAnswer(invocation -> {
                    Usuario u = invocation.getArgument(0);
                    assertEquals(1L, u.getRolId());
                    assertEquals(1L, u.getRolId());
                    return Mono.just(u);
                });

        StepVerifier.create(usuarioUseCase.registrarUsuario(usuario))
                .expectNextMatches(u -> u.getRolId().equals(1L))
                .verifyComplete();
    }

    @Test
    void shouldKeepExistingRolWhenNotNull() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test2@test.com");
        usuario.setDocumentoIdentidad("67890");
        usuario.setRolId(2L);

        when(usuarioRepository.existsByEmail(usuario.getEmail()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existsByDocumentoIdentidad(usuario.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.registrarUsuario(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(usuarioUseCase.registrarUsuario(usuario))
                .expectNextMatches(u -> u.getRolId().equals(2L))
                .verifyComplete();
    }


}