package com.medilabo.service;

import com.medilabo.model.Patient;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for patient management operations
 */
public interface IPatientService {

    /**
     * Retrieves all patients from the system
     * @return List of all patients
     */
    List<Patient> getAllPatients();

    /**
     * Retrieves a specific patient by ID
     * @param id The patient identifier
     * @return Optional containing the patient if found
     */
    Optional<Patient> getPatientById(String id);

    /**
     * Adds a new patient to the system
     * @param patient The patient to add
     * @return The added patient
     */
    Patient addPatient(Patient patient);

    /**
     * Updates an existing patient's information
     * @param id The patient identifier
     * @param patient The updated patient information
     * @return Optional containing the updated patient if found
     */
    Optional<Patient> updatePatient(String id, Patient patient);
}
