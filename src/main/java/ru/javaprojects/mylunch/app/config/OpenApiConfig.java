package ru.javaprojects.mylunch.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Приложение <a href='https://jino/mylunch'>My Lunch</a>
                        <p><b>Тестовые учётные записи авторизации:</b><br>
                        - user@yandex.ru / password<br>
                        - admin@gmail.com / admin<br>
                        - guest@gmail.com / guest</p>
                        
                        <p><b>Тестовые данные:</b><br>
                        - resources/data.sql
                        """,
                contact = @Contact(url = "https://jino/mylunch/#contacts", name = "Vladimit Rodionov", email = "rodionov.java@mail.ru")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}
