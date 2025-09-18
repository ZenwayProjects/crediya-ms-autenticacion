package co.com.zenway.usecase.usuario;

import co.com.zenway.model.rol.Rol;
import co.com.zenway.model.rol.gateways.RolRepository;
import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.PasswordEncoder;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import co.com.zenway.usecase.usuario.exception.DocumentoDeIdentidadEnUso;
import co.com.zenway.usecase.usuario.exception.EmailEnUso;
import co.com.zenway.usecase.usuario.utils.Constantes;
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
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RegistroUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private Usuario usuarioTest;

    @Mock
    private RegistroUsuarioUseCase registroUsuarioUseCase;

    @Mock
    private  PasswordEncoder passwordEncoder;




    @BeforeEach
    void setup() {
        registroUsuarioUseCase = new RegistroUsuarioUseCase(usuarioRepository,rolRepository, passwordEncoder );

        usuarioTest = Usuario.builder()
                .id(150L)
                .nombre("German")
                .apellido("Perez")
                .email("germanperez@gmail.com")
                .documentoIdentidad("193204829")
                .telefono("3163278354")
                .rolId((short) 2)
                .salarioBase( new BigDecimal("100000"))
                .build();
    }

    @Test
    void shouldRegistrarUsuarioWhenEmailIsNotUsedAndRolIsNull() {
        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setId((short) 99);
        rolPorDefecto.setNombre("ROL_POR_DEFECTO");

        Usuario usuarioSinRol = new Usuario();
        usuarioSinRol.setEmail("test@test.com");
        usuarioSinRol.setDocumentoIdentidad("12345");
        usuarioSinRol.setRolId(null);

        when(usuarioRepository.existsByEmail(usuarioSinRol.getEmail()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existsByDocumentoIdentidad(usuarioSinRol.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));
        when(rolRepository.buscarRolPorDefecto())
                .thenReturn(Mono.just(rolPorDefecto));
        when(usuarioRepository.registrarUsuario(any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(registroUsuarioUseCase.registrarUsuario(usuarioSinRol))
                .expectNextMatches(u -> u.getRolId().equals((short) 99))
                .verifyComplete();
    }



    @Test
    void shouldAssignRolUsuarioWhenRolIsNull() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setDocumentoIdentidad("12345232");
        usuario.setRolId(null);

        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setId((short) 2);
        rolPorDefecto.setNombre("ROL_USUARIO");

        when(usuarioRepository.existsByEmail(usuario.getEmail()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existsByDocumentoIdentidad(usuario.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));
        when(rolRepository.buscarRolPorDefecto())
                .thenReturn(Mono.just(rolPorDefecto)); // <-- ahora sí correcto
        when(usuarioRepository.registrarUsuario(any()))
                .thenAnswer(invocation -> {
                    Usuario u = invocation.getArgument(0);
                    assertEquals((short) 2, u.getRolId());
                    return Mono.just(u);
                });

        StepVerifier.create(registroUsuarioUseCase.registrarUsuario(usuario))
                .expectNextMatches(u -> u.getRolId().equals((short) 2))
                .verifyComplete();
    }


    @Test
    void shouldKeepExistingRolWhenNotNull() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test2@test.com");
        usuario.setDocumentoIdentidad("67890");
        usuario.setRolId((short) 2);

        when(usuarioRepository.existsByEmail(usuario.getEmail()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existsByDocumentoIdentidad(usuario.getDocumentoIdentidad()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.registrarUsuario(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(registroUsuarioUseCase.registrarUsuario(usuario))
                .expectNextMatches(u -> u.getRolId().equals((short) 2))
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenEmailInUse() {
        Usuario usuario = new Usuario();
        usuario.setEmail("existente@test.com");
        usuario.setDocumentoIdentidad("12345");

        when(usuarioRepository.existsByEmail(usuario.getEmail()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(registroUsuarioUseCase.registrarUsuario(usuario))
                .expectErrorMatches(e -> e instanceof EmailEnUso &&
                        e.getMessage().equals(Constantes.EMAIL_EN_USO))
                .verify();

        // No se llama a existsByDocumentoIdentidad ni registrarUsuario
        verify(usuarioRepository, never()).existsByDocumentoIdentidad(any());
        verify(usuarioRepository, never()).registrarUsuario(any());
    }

    @Test
    void shouldThrowErrorWhenDocumentoInUse() {
        Usuario usuario = new Usuario();
        usuario.setEmail("nuevo@test.com");
        usuario.setDocumentoIdentidad("existente");

        when(usuarioRepository.existsByEmail(usuario.getEmail()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existsByDocumentoIdentidad(usuario.getDocumentoIdentidad()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(registroUsuarioUseCase.registrarUsuario(usuario))
                .expectErrorMatches(e -> e instanceof DocumentoDeIdentidadEnUso &&
                        e.getMessage().equals(Constantes.DOCUMENTO_IDENTIDAD_EN_USO))
                .verify();

        // No se llama a registrarUsuario
        verify(usuarioRepository, never()).registrarUsuario(any());
    }





}