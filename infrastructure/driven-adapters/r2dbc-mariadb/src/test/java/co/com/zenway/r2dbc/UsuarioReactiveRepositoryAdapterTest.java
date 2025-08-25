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
    // TODO: change four you own tests

    @InjectMocks
    UsuarioReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {

        when(repository.findById(Long.valueOf("1"))).thenReturn(Mono.just(new UsuarioEntity()));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Usuario> result = repositoryAdapter.findById(Long.valueOf("1"));

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just(new UsuarioEntity()));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Usuario> result = repositoryAdapter.findByExample(new Usuario());

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repository.save(new UsuarioEntity())).thenReturn(Mono.just(new UsuarioEntity()));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Usuario> result = repositoryAdapter.save(new Usuario());

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }
}
