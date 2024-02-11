package az.najafov.deforestationnews.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SwaggerConfiguration {


    @Bean
    @Profile({"default"})
    public OpenAPI openApiInformationLocal() {
        return getOpenAPI();
    }

    private OpenAPI getOpenAPI() {
        return new OpenAPI().components(getComponents()).info(getInfo())
                .addSecurityItem(new SecurityRequirement().addList("bearerToken"));
    }

    private Info getInfo() {
        Contact contact = new Contact()
                .name("Najafov");
        return new Info()
                .contact(contact)
                .description("API documentation of deforestation news.")
                .title("pcs")
                .version("V1.0.0")
                .license(new License().name("Apache 2.0").url("https://springdoc.org"));
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes("bearerToken", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );
    }


}
