package com.medilabo.service;

import com.medilabo.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of patient service
 */
@Service
public class PatientService implements IPatientService {

    private final List<Patient> patients = new ArrayList<>();

    @Override
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    @Override
    public Optional<Patient> getPatientById(String id) {
        return patients.stream()
                .filter(patient -> patient.getId().equals(id))
                .findFirst();
    }

    @Override
    public Patient addPatient(Patient patient) {
        patients.add(patient);
        return patient;
    }

    @Override
    public Optional<Patient> updatePatient(String id, Patient patient) {
        Optional<Patient> existingPatient = getPatientById(id);
        if (existingPatient.isPresent()) {
            patients.removeIf(p -> p.getId().equals(id));
            patient.setId(id);
            patients.add(patient);
            return Optional.of(patient);
        }
        return Optional.empty();
    }
}
