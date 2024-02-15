package io.github.douglasliebl.authserver.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.setName("Douglas Liebl");
        contact.setEmail("douglasliebl@outlook.com");
        contact.setUrl("github.com/DouglasLiebl");

        Info info = new Info()
                .title("Auth-Server")
                .description("Authentication and User management service.")
                .version("0.0.1")
                .contact(contact);

        return new OpenAPI()
                .info(info);
    }
}
