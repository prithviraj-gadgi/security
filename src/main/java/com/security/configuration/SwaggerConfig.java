package com.security.configuration;

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
                        .title("JWT Security")
                        .version("0.0.1-SNAPSHOT")
                        .description("Application for Spring Security")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .name("Prithviraj Gadgi")
                                .url("https://www.linkedin.com/in/prithviraj-gadgi-44694515a/")
                                .email("prithvirajgadgi@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
