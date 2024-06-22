package com.saber.apigateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Value(value = "${springdoc.oAuthFlow.authorizationUrl}")
    private String authorizationUrl;
    @Value(value = "${springdoc.oAuthFlow.tokenUrl}")
    private String tokenUrl;
    @Bean
    public OpenAPI openAPI(
            @Value(value = "${service.swagger.title}") String swaggerTitle,
            @Value(value = "${service.swagger.version}") String swaggerVersion,
            @Value(value = "${service.swagger.description}") String swaggerDescription
    ) {
        String bearerSecuritySchema = "keycloak-authorize";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(bearerSecuritySchema))
                .components(new Components()
                        .addSecuritySchemes(bearerSecuritySchema, new SecurityScheme()
                                .name(bearerSecuritySchema)
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows().authorizationCode(
                                        new OAuthFlow().authorizationUrl(authorizationUrl).tokenUrl(tokenUrl)
                                                .scopes(new Scopes()
                                                        .addString("openid", "openid")
                                                        .addString("read", "read")
                                                        .addString("write", "write")
                                                )
                                ))))
                .info(new Info().title(swaggerTitle)
                        .version(swaggerVersion)
                        .description(swaggerDescription));
    }
}