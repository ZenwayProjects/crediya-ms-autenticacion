package co.com.zenway.api.docs;

import co.com.zenway.api.UsuarioHandler;
import co.com.zenway.api.dto.UsuarioRegistroDTO;
import co.com.zenway.api.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UsuarioRouterDoc {

    @Bean
    @RouterOperation(
            path = "/api/v1/usuarios",
            produces = {"application/json; charset=UTF-8"},
            method = RequestMethod.POST,
            beanClass = UsuarioHandler.class,
            beanMethod = "registrarUsuario",
            operation = @Operation(
                    operationId = "crearUsuario",
                    summary = "Crea un nuevo usuario",
                    description = "Recibe los datos de un usuario y devuelve el usuario creado",
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioRegistroDTO.class)
                            )
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Usuario creado correctamente",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Parametros invalidos o faltantes",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class)
                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "409",
                                    description = "Usuario ya existe",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class)
                                    )
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> usuarioRoutes(UsuarioHandler usuarioHandler) {
        return route(POST("/api/v1/usuarios"), usuarioHandler::registrarUsuario);
    }
}
