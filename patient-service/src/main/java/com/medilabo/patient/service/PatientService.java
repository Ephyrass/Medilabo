package com.medilabo.patient.service;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Patient management service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    /**
     * View personal information of patients
     *
     * @return list of all patients
     */
    public List<Patient> getAllPatients() {
        log.info("Fetching all patients");
        return patientRepository.findAll();
    }

    /**
     * View personal information of a patient
     *
     * @param id the patient ID
     * @return optional containing the patient if found
     */
    public Optional<Patient> getPatientById(Long id) {
        log.info("Fetching patient with ID: {}", id);
        return patientRepository.findById(id);
    }

    /**
     * Add personal information of patients
     *
     * @param patient the patient to add
     * @return the saved patient
     */
    public Patient addPatient(Patient patient) {
        log.info("Adding new patient: {} {}", patient.getFirstName(), patient.getLastName());
        return patientRepository.save(patient);
    }

    /**
     * Update personal information
     *
     * @param id the patient ID
     * @param patientDetails the updated patient details
     * @return optional containing the updated patient if found
     */
    public Optional<Patient> updatePatient(Long id, Patient patientDetails) {
        log.info("Updating patient with ID: {}", id);
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setFirstName(patientDetails.getFirstName());
                    patient.setLastName(patientDetails.getLastName());
                    patient.setBirthDate(patientDetails.getBirthDate());
                    patient.setGender(patientDetails.getGender());
                    if (patientDetails.getContactInfo() != null) {
                        patient.setContactInfo(patientDetails.getContactInfo());
                    }
                    return patientRepository.save(patient);
                });
    }

    /**
     * Delete a patient
     *
     * @param id the patient ID
     * @return true if deleted, false if not found
     */
    public boolean deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

