package com.medilabo.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Note Service - Sprint 2
 * Microservice for managing medical notes with MongoDB
 */
@SpringBootApplication
public class NoteServiceApplication {

    /**
     * Main method to start the Note Service application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(NoteServiceApplication.class, args);
    }
}

