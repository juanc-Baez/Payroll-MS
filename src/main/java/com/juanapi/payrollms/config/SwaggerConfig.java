package com.juanapi.payrollms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payroll Management System API")
                        .version("1.0.0")
                        .description("API para la gestión de nómina de empleados. Permite crear, consultar, actualizar y eliminar empleados, así como generar nóminas automáticamente.")
                        .contact(new Contact()
                                .name("Juan Baez")
                                .email("juanbaezc8@gmail.com")
                                .url("https://github.com/juanc-Baez"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
