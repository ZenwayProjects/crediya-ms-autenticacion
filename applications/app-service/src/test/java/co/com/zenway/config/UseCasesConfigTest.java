package co.com.zenway.config;

import co.com.zenway.model.usuario.Usuario;
import co.com.zenway.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        // Stub repository to satisfy UseCase constructor dependencies discovered by @ComponentScan
        @Bean
        public UsuarioRepository usuarioRepository() {
            return new UsuarioRepository() {

                @Override
                public Mono<Usuario> registrarUsuario(Usuario usuario) {
                    return Mono.just(usuario);
                }
                @Override
                public Mono<Boolean> existsByEmail(String email) {
                    return Mono.just(false);
                }

                @Override
                public Mono<Boolean> existsByDocumentoIdentidad(String documentoIdentidad) {
                    return null;
                }

                @Override
                public Mono<Usuario> findByEmail(String email) {
                    return null;
                }

                @Override
                public Mono<Usuario> findById(Long usuarioId) {
                    return null;
                }
            };
        }

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}