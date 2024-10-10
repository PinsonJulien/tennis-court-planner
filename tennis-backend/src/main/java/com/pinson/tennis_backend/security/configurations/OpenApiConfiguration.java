package com.pinson.tennis_backend.security.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Documentation",
        version = "v1"
    ),
    security = @SecurityRequirement(
        name = "bearerAuth"
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfiguration {

    @Bean
    public OpenApiCustomizer customOpenApiCustomizer() {
        return openApi -> {
            Parameter contextParameter =  new QueryParameter()
                .name("context")
                .schema(new StringSchema())
                .required(false)
                .description("Context of the request");

            openApi.getComponents().addParameters(contextParameter.getName(), contextParameter);
            openApi.getPaths().values().forEach(pathItem -> {
                pathItem.readOperations().forEach(operation -> {
                    operation.addParametersItem(contextParameter);
                });
            });
        };
    }


}
