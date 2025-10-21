package com.medilabo.patient.config;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Initialisation des données de test - Sprint 1
 * Charge les 4 cas de test patients
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;

    @Override
    public void run(String... args) {
        if (patientRepository.count() == 0) {
            log.info("Initialisation des données de test - 4 cas de test patients");

            // Patient 1: TestNone - Aucun risque
            Patient patient1 = new Patient();
            patient1.setFirstName("TestNone");
            patient1.setLastName("Test");
            patient1.setBirthDate(LocalDate.of(1966, 12, 31));
            patient1.setGender("F");
            patient1.setAddress("1 Brookside St");
            patient1.setPhoneNumber("100-222-3333");
            patientRepository.save(patient1);

            // Patient 2: TestBorderline - Risque limité
            Patient patient2 = new Patient();
            patient2.setFirstName("TestBorderline");
            patient2.setLastName("Test");
            patient2.setBirthDate(LocalDate.of(1945, 6, 24));
            patient2.setGender("M");
            patient2.setAddress("2 High St");
            patient2.setPhoneNumber("200-333-4444");
            patientRepository.save(patient2);

            // Patient 3: TestInDanger - En danger
            Patient patient3 = new Patient();
            patient3.setFirstName("TestInDanger");
            patient3.setLastName("Test");
            patient3.setBirthDate(LocalDate.of(2004, 6, 18));
            patient3.setGender("M");
            patient3.setAddress("3 Club Road");
            patient3.setPhoneNumber("300-444-5555");
            patientRepository.save(patient3);

            // Patient 4: TestEarlyOnset - Apparition précoce
            Patient patient4 = new Patient();
            patient4.setFirstName("TestEarlyOnset");
            patient4.setLastName("Test");
            patient4.setBirthDate(LocalDate.of(2002, 6, 28));
            patient4.setGender("F");
            patient4.setAddress("4 Valley Dr");
            patient4.setPhoneNumber("400-555-6666");
            patientRepository.save(patient4);

            log.info("Données de test initialisées: {} patients créés", patientRepository.count());
        } else {
            log.info("Base de données déjà initialisée avec {} patients", patientRepository.count());
        }
    }
}

