package com.medilabo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**- Sprint 1
 * Single entry point for all Medilabo microservices
 */
@SpringBootApplication
public class GatewayApplication {

	/**
	 * Main method to start the Gateway application
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}

