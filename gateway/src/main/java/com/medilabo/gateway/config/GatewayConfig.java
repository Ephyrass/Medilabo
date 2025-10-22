package com.medilabo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration du routage Gateway - Sprint 1
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route vers Patient Service (Sprint 1)
                .route("patient-service", r -> r
                        .path("/api/patients/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("patientServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/patients"))
                                .retry(config -> config.setRetries(3)))
                        .uri("http://patient-service:8081"))

                // Route vers Note Service (Sprint 2)
                .route("note-service", r -> r
                        .path("/api/notes/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("noteServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/notes"))
                                .retry(config -> config.setRetries(3)))
                        .uri("http://note-service:8082"))

                // Route vers Risk Assessment Service (Sprint 3)
                .route("risk-service", r -> r
                        .path("/api/risk/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("riskServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/risk"))
                                .retry(config -> config.setRetries(3)))
                        .uri("http://risk-service:8083"))

                .build();
    }
}

