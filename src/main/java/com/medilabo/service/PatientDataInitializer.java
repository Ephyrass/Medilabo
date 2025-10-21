package com.medilabo.service;

import com.medilabo.model.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Data initializer to load test patients for Sprint 1
 * Runs automatically on application startup (only in non-test profiles)
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
@Order(1) // Execute before NoteDataInitializer
public class PatientDataInitializer implements CommandLineRunner {

    private final PatientService patientService;

    @Override
    public void run(String... args) {
        log.info("Initializing Sprint 1 test data - 4 test patients");

        // Patient 1 - TestNone (No diabetes risk)
        patientService.addPatient(new Patient("1", "TestNone", "Test",
                LocalDate.of(1966, 12, 31), "F",
                "1 Brookside St", "100-222-3333"));

        // Patient 2 - TestBorderline (Borderline risk)
        patientService.addPatient(new Patient("2", "TestBorderline", "Test",
                LocalDate.of(1945, 6, 24), "M",
                "2 High St", "200-333-4444"));

        // Patient 3 - TestInDanger (In danger risk)
        patientService.addPatient(new Patient("3", "TestInDanger", "Test",
                LocalDate.of(2004, 6, 18), "M",
                "3 Club Road", "300-444-5555"));

        // Patient 4 - TestEarlyOnset (Early onset risk)
        patientService.addPatient(new Patient("4", "TestEarlyOnset", "Test",
                LocalDate.of(2002, 6, 28), "F",
                "4 Valley Dr", "400-555-6666"));

        log.info("Successfully initialized {} test patients", patientService.getAllPatients().size());
    }
}

