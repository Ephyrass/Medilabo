package com.medilabo.service;

import com.medilabo.model.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of patient service
 * Contains test data for Sprint 1
 */
@Service
public class PatientService implements IPatientService {

    private final List<Patient> patients = new ArrayList<>();

    public PatientService() {
        // Test data for Sprint 1 (4 test cases)
        patients.add(new Patient("1", "TestNone", "Test", LocalDate.of(1966, 12, 31), "F"));
        patients.add(new Patient("2", "TestBorderline", "Test", LocalDate.of(1945, 6, 24), "M"));
        patients.add(new Patient("3", "TestInDanger", "Test", LocalDate.of(2004, 6, 18), "M"));
        patients.add(new Patient("4", "TestEarlyOnset", "Test", LocalDate.of(2002, 6, 28), "F"));
    }

    @Override
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }
}
