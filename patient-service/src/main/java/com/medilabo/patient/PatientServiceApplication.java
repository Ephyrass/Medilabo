package com.medilabo.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Patient Service
 * Microservice for managing patient records with MongoDB
 */
@SpringBootApplication
public class PatientServiceApplication {

	/**
	 * Main method to start the Patient Service application
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PatientServiceApplication.class, args);
	}

}

