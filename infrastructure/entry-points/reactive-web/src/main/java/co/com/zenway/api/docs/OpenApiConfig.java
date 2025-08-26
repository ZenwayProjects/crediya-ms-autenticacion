package co.com.zenway.api.docs;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Crediya Auth MS",
                version = "1.0.0",
                description = "API for PowerUpBootcamp",
                contact = @Contact(
                        name = "PowerUp Team",
                        email = "admonpowerup@pragma.com.co",
                        url = "https://pragma.co"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class OpenApiConfig {

}
