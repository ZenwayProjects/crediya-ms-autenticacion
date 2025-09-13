package co.com.zenway.api.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static co.com.zenway.api.utils.ConstantesErrores.*;

@Component
public class GlobalErrorHandler {
    private static final String MENSAJE = "mensaje";
    private static final String ERROR = "error";


    public Mono<ServerResponse> handler(Throwable exception){
        return switch (exception) {
            case ConstraintViolationException violationException -> constraintViolationHandler(violationException);
            case IllegalArgumentException ie -> illegalArgumentHandler(ie);
            default -> genericErrorHandler(exception);
        };
    }

    private Mono<ServerResponse> genericErrorHandler(Throwable e){
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(Map.of(
                        ERROR, ERROR_INTERNO,
                        MENSAJE, e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado"
                ));

    }

    private Mono<ServerResponse> constraintViolationHandler(ConstraintViolationException e){
        var constraintViolations =e.getConstraintViolations().stream()
                .map(c -> Map.of("campo", c.getPropertyPath().toString(),
                        MENSAJE, c.getMessage()))
                .toList();

        return ServerResponse.badRequest().bodyValue(Map.of(
                ERROR, VALIDACION_FALLIDA,
                "violacion", constraintViolations));
    }

    private Mono<ServerResponse> illegalArgumentHandler(IllegalArgumentException illegalArgumentException){
        return ServerResponse.badRequest().bodyValue(Map.of(
                ERROR, ARGUMENTO_NO_VALIDO,
                MENSAJE,illegalArgumentException.getMessage()
        ));

    }
}
