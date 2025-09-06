package co.com.zenway.api;

import co.com.zenway.api.dto.UsuarioRegistroDTO;
import co.com.zenway.api.dto.UsuarioResponseDTO;
import co.com.zenway.api.mapper.UsuarioMapper;
import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.usecase.usuario.RegistroUsuarioUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UsuarioHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegistroUsuarioUseCase registroUsuarioUseCase;

    @MockitoBean
    private UsuarioMapper usuarioMapper;

    @MockitoBean
    private Validator validator;

    @MockitoBean
    private GlobalErrorHandler globalErrorHandler;


    @Test
    void testListenPOSTUseCase() {
        UsuarioRegistroDTO request = new UsuarioRegistroDTO();
        request.setNombre("Jorge");
        request.setApellido("Perez");
        request.setEmail("jorgeperez@gmail.com");
        request.setDocumentoIdentidad("765738929");
        request.setTelefono("313267458");
        request.setSalarioBase(BigDecimal.valueOf(1800000));
        request.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        Usuario usuarioModel = new Usuario();
        usuarioModel.setNombre("Jorge");
        usuarioModel.setApellido("Perez");
        usuarioModel.setEmail("jorgeperez@gmail.com");
        usuarioModel.setDocumentoIdentidad("765738929");
        usuarioModel.setTelefono("313267458");
        usuarioModel.setSalarioBase(BigDecimal.valueOf(1800000));
        usuarioModel.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        UsuarioResponseDTO usuarioResponse = new UsuarioResponseDTO();
        usuarioResponse.setNombre("Jorge");
        usuarioResponse.setApellido("Perez");
        usuarioResponse.setEmail("jorgeperez@gmail.com");
        usuarioResponse.setRolId((short)(2));

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(usuarioMapper.toModel(any())).thenReturn(usuarioModel);
        when(registroUsuarioUseCase.registrarUsuario(any())).thenReturn(Mono.just(usuarioModel));
        when(usuarioMapper.toResponse(any())).thenReturn(usuarioResponse);

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDTO.class)
                .value(resp ->
                        assertThat(resp.getNombre()).isEqualTo("Jorge")
                );
    }

}
