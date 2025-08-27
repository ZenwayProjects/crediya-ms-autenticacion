package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;


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
        Mockito.when(usuarioRepository.existsByEmail(usuarioTest.getEmail())).thenReturn(Mono.just(false));
        Mockito.when(usuarioRepository.registrarUsuario(usuarioTest)).thenReturn(Mono.just(usuarioTest));
        Mockito.when(usuarioRepository.existsByDocumentoIdentidad(usuarioTest.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(usuarioUseCase.registrarUsuario(usuarioTest))
                .expectNext(usuarioTest)
                .verifyComplete();

    }


}