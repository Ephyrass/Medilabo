package com.medilabo.risk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Risk Assessment Service
 * Microservice for assessing diabetes risk based on patient data and medical notes
 */
@SpringBootApplication
public class RiskServiceApplication {

    /**
     * Main method to start the Risk Service application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RiskServiceApplication.class, args);
    }
}

