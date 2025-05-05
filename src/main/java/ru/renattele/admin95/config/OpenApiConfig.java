package ru.renattele.admin95.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI documentation
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI adminApiDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title("Admin95 API Documentation")
                        .description("API documentation for Admin95 container management system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Admin95")
                                .url("https://github.com/renattele/admin95")
                                .email("contact@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Admin95 Documentation")
                        .url("https://github.com/renattele/admin95"))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL")
                ));
    }
}
