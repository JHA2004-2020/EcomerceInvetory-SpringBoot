package com.EcomerceInventory.EcomerceInvetory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Inventario y E-commerce")
                        .version("1.0")
                        .description("Documentación de la API para el sistema de facturación y control de stock.")
                        .contact(new Contact().name("Jeronimo Henao Arango").email("jeronimohenaoarango376@gmail.com")));
    }
}