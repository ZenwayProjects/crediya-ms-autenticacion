package co.com.zenway.usecase.usuario;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import co.com.zenway.usecase.usuario.exception.DocumentoNoExiste;
import co.com.zenway.usecase.usuario.utils.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;


class SolicitudUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SolicitudUseCase solicitudUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@mail.com");
        usuario.setDocumentoIdentidad("12345");
    }

    @Test
    void obtenerUsuarioIdYEmailPorDocumento_documentoExiste() {
        when(usuarioRepository.existsByDocumentoIdentidad("12345")).thenReturn(Mono.just(true));
        when(usuarioRepository.obtenerIdYEmailPorDocumentoIdentidad("12345")).thenReturn(Mono.just(usuario));

        StepVerifier.create(solicitudUseCase.obtenerUsuarioIdYEmailPorDocumento("12345"))
                .expectNextMatches(dto -> dto.idUsuario().equals(1L) && dto.email().equals("test@mail.com"))
                .verifyComplete();

        verify(usuarioRepository).existsByDocumentoIdentidad("12345");
        verify(usuarioRepository).obtenerIdYEmailPorDocumentoIdentidad("12345");
    }

    @Test
    void obtenerUsuarioIdYEmailPorDocumento_documentoNoExiste() {
        when(usuarioRepository.existsByDocumentoIdentidad("99999")).thenReturn(Mono.just(false));

        StepVerifier.create(solicitudUseCase.obtenerUsuarioIdYEmailPorDocumento("99999"))
                .expectErrorMatches(e -> e instanceof DocumentoNoExiste &&
                        e.getMessage().equals(Constantes.DOCUMENTO_IDENTIDAD_NO_REGISTRADO))
                .verify();

        verify(usuarioRepository).existsByDocumentoIdentidad("99999");
        verify(usuarioRepository, never()).obtenerIdYEmailPorDocumentoIdentidad(anyString());
    }

    @Test
    void obtenerUsuariosPorEmails_listaConUsuarios() {
        List<String> emails = List.of("test@mail.com", "other@mail.com");
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setEmail("other@mail.com");

        when(usuarioRepository.buscarPorEmails(emails)).thenReturn(Flux.just(usuario, otroUsuario));

        StepVerifier.create(solicitudUseCase.obtenerUsuariosPorEmails(emails))
                .expectNextMatches(u -> u.getEmail().equals("test@mail.com"))
                .expectNextMatches(u -> u.getEmail().equals("other@mail.com"))
                .verifyComplete();

        verify(usuarioRepository).buscarPorEmails(emails);
    }

    @Test
    void obtenerUsuariosPorEmails_listaVacia() {
        when(usuarioRepository.buscarPorEmails(List.of())).thenReturn(Flux.empty());

        StepVerifier.create(solicitudUseCase.obtenerUsuariosPorEmails(List.of()))
                .verifyComplete();

        verify(usuarioRepository).buscarPorEmails(List.of());
    }
}
