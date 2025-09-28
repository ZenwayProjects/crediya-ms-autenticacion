package co.com.zenway.r2dbc;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.r2dbc.adapter.UsuarioReactiveRepository;
import co.com.zenway.r2dbc.adapter.UsuarioReactiveRepositoryAdapter;
import co.com.zenway.r2dbc.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioReactiveRepositoryAdapterTest {

    @InjectMocks
    UsuarioReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        UsuarioEntity entity = new UsuarioEntity();
        Usuario usuario = new Usuario();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UsuarioEntity entity = new UsuarioEntity();
        Usuario usuario = new Usuario();

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        Usuario example = new Usuario();
        UsuarioEntity entity = new UsuarioEntity();
        Usuario usuario = new Usuario();

        when(mapper.map(example, UsuarioEntity.class)).thenReturn(entity);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findByExample(example);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        Usuario usuario = new Usuario();
        UsuarioEntity entity = new UsuarioEntity();

        when(mapper.map(usuario, UsuarioEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.save(usuario);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }
}
