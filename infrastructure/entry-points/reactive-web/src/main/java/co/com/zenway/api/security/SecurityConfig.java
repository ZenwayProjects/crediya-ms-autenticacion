package co.com.zenway.api.security;

import co.com.zenway.api.exceptions.ReactiveSecurityExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveSecurityExceptionHandler reactiveSecurityExceptionHandler;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http, ReactiveJwtDecoder jwtDecoder,
            ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(
                                "/actuator/**",
                                "/health",
                                "/api/v1/login",
                                "/webjars/swagger-ui/**",
                                "/v3/api-docs/**",
                                "swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST,"/api/v1/usuarios").hasAnyRole("ADMINISTRADOR", "ASESOR")
                        .pathMatchers("/api/v1/usuarios/email/{documento}").hasRole("CLIENTE")
                        .pathMatchers("/api/v1/usuarios-por-emails").hasAnyRole("ADMINISTRADOR", "ASESOR")
                        .anyExchange().authenticated()
                )
                .exceptionHandling(ex ->
                        ex.accessDeniedHandler(reactiveSecurityExceptionHandler))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtDecoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter))
                                .authenticationEntryPoint(reactiveSecurityExceptionHandler)
                )
                .build();
    }
}
