package com.medilabo.patient.config;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Initialisation des données de test - Sprint 1 & 2
 * Charge les 4 patients de test pour les évaluations de risque
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;

    @Override
    public void run(String... args) {
        // Vérifier si les patients de test existent déjà
        if (patientRepository.count() < 4) {
            log.info("Initialisation des 4 patients de test");

            // Patient 1: TestNone - Aucun risque
            createPatient("Test", "TestNone", LocalDate.of(1966, 12, 31), "F",
                "1 Brookside St", "100-222-3333");

            // Patient 2: TestBorderline - Risque limité
            createPatient("Test", "TestBorderline", LocalDate.of(1945, 6, 24), "M",
                "2 High St", "200-333-4444");

            // Patient 3: TestInDanger - En danger
            createPatient("Test", "TestInDanger", LocalDate.of(2004, 6, 18), "M",
                "3 Club Road", "300-444-5555");

            // Patient 4: TestEarlyOnset - Apparition précoce
            createPatient("Test", "TestEarlyOnset", LocalDate.of(2002, 6, 28), "F",
                "4 Valley Dr", "400-555-6666");

            log.info("4 patients de test créés avec succès");
        } else {
            log.info("Patients de test déjà existants dans la base de données");
        }
    }

    private void createPatient(String firstName, String lastName, LocalDate birthDate,
                              String gender, String address, String phoneNumber) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setBirthDate(birthDate);
        patient.setGender(gender);
        patient.setAddress(address);
        patient.setPhoneNumber(phoneNumber);
        patientRepository.save(patient);
        log.debug("Patient créé: {} {}", firstName, lastName);
    }
}

