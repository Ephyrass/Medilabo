package com.medilabo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway routing configuration
 */
@Configuration
public class GatewayConfig {

    /**
     * Configures custom routes for all microservices with circuit breaker and retry patterns
     *
     * @param builder the route locator builder
     * @return the configured route locator
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route to Patient Service
                .route("patient-service", r -> r
                        .path("/api/patients/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("patientServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/patients"))
                                .retry(config -> config.setRetries(3)))
                        .uri("http://patient-service:8081"))

                // Route to Note Service
                .route("note-service", r -> r
                        .path("/api/notes/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("noteServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/notes"))
                                .retry(config -> config.setRetries(3)))
                        .uri("http://note-service:8082"))

                // Route to Risk Assessment Service
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

