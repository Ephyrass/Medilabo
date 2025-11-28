package com.medilabo.patient.config;

import com.medilabo.patient.model.ContactInfo;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Test data initialization
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;

    @Override
    public void run(String... args) {
        if (patientRepository.count() < 4) {
            log.info("Initializing 4 test patients");

            createPatient("Test", "TestNone", LocalDate.of(1966, 12, 31), "F",
                "1 Brookside St", "100-222-3333");

            createPatient("Test", "TestBorderline", LocalDate.of(1945, 6, 24), "M",
                "2 High St", "200-333-4444");

            createPatient("Test", "TestInDanger", LocalDate.of(2004, 6, 18), "M",
                "3 Club Road", "300-444-5555");

            createPatient("Test", "TestEarlyOnset", LocalDate.of(2002, 6, 28), "F",
                "4 Valley Dr", "400-555-6666");

            log.info("4 test patients created successfully");
        } else {
            log.info("Test patients already exist in database");
        }
    }

    /**
     * Creates and saves a patient with contact information
     *
     * @param firstName the patient's first name
     * @param lastName the patient's last name
     * @param birthDate the patient's birth date
     * @param gender the patient's gender
     * @param address the patient's address
     * @param phoneNumber the patient's phone number
     */
    private void createPatient(String firstName, String lastName, LocalDate birthDate,
                              String gender, String address, String phoneNumber) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setBirthDate(birthDate);
        patient.setGender(gender);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setAddress(address);
        contactInfo.setPhoneNumber(phoneNumber);

        patient.setContactInfo(contactInfo);
        contactInfo.setPatient(patient);

        patientRepository.save(patient);
        log.debug("Patient created: {} {}", firstName, lastName);
    }
}

