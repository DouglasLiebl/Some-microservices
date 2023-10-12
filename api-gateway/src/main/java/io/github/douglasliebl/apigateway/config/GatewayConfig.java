package io.github.douglasliebl.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/product/**").uri("lb://ms-products"))
                .route(r -> r.path("/manufacturer/**").uri("lb://ms-products"))
                .route(r -> r.path("/category/**").uri("lb://ms-products"))
                .route(r -> r.path("/oauth2/**").uri("lb://auth-server"))
                .build();
    }
}
