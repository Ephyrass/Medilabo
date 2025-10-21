package com.medilabo.patient.repository;

import com.medilabo.patient.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour les patients - Sprint 1 (MongoDB)
 */
@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findByLastNameAndFirstName(String lastName, String firstName);

    List<Patient> findByLastName(String lastName);
}

