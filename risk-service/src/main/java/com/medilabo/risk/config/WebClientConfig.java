package com.medilabo.risk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for WebClient to communicate with other microservices
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a WebClient builder bean
     *
     * @return the WebClient builder
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

