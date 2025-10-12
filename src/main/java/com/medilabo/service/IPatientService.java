package com.medilabo.service;

import com.medilabo.model.Patient;

import java.util.List;

/**
 * Service interface for patient management operations
 */
public interface IPatientService {

    /**
     * Retrieves all patients from the system
     * @return List of all patients
     */
    List<Patient> getAllPatients();
}

