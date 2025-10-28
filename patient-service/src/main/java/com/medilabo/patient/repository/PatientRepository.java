package com.medilabo.patient.repository;

import com.medilabo.patient.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for patients
 */
@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    /**
     * Find a patient by last name and first name
     *
     * @param lastName the patient's last name
     * @param firstName the patient's first name
     * @return optional containing the patient if found
     */
    Optional<Patient> findByLastNameAndFirstName(String lastName, String firstName);

    /**
     * Find all patients by last name
     *
     * @param lastName the patient's last name
     * @return list of patients with the given last name
     */
    List<Patient> findByLastName(String lastName);
}

