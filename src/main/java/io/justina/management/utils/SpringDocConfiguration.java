package io.justina.management.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Clase de configuración para la documentación de OpenAPI en la aplicación Spring.
 */
@Configuration
public class SpringDocConfiguration {


    /**
     * Configura y devuelve un objeto OpenAPI personalizado para la documentación de la API.
     *
     * @return Objeto OpenAPI configurado con esquema de seguridad Bearer JWT y metadatos de la API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API Justina")
                        .description("Justina app Rest API, containing doctor and patient CRUD capabilities.")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Equip Backend")
                                .email("backend@justina.io"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("https://justina.io/api/licencia")));
    }
}
