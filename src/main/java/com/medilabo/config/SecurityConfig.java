package com.medilabo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for the application (WebFlux/Reactive)
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures security filter chain for reactive applications
     * Disables CSRF and allows all requests without authentication (for testing purposes)
     *
     * @param http ServerHttpSecurity configuration object
     * @return Configured SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll())
                .build();
    }
}
