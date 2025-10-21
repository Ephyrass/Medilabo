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
 * Service de gestion des patients - Sprint 1
 * Implémente les user stories du premier sprint
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    /**
     * Vue des infos personnelles des patients
     * User Story: En tant qu'organisateur, j'aimerais voir les informations personnelles de mes patients
     */
    public List<Patient> getAllPatients() {
        log.info("Récupération de tous les patients");
        return patientRepository.findAll();
    }

    /**
     * Vue des infos personnelles d'un patient
     */
    public Optional<Patient> getPatientById(String id) {
        log.info("Récupération du patient avec l'ID: {}", id);
        return patientRepository.findById(id);
    }

    /**
     * Ajouter des informations personnelles des patients
     * User Story: En tant qu'organisateur, j'aimerais ajouter des informations personnelles aux patients
     */
    public Patient addPatient(Patient patient) {
        log.info("Ajout d'un nouveau patient: {} {}", patient.getFirstName(), patient.getLastName());
        return patientRepository.save(patient);
    }

    /**
     * Mise à jour des informations personnelles
     * User Story: En tant qu'organisateur, j'aimerais mettre à jour les informations personnelles de mes patients
     */
    public Optional<Patient> updatePatient(String id, Patient patientDetails) {
        log.info("Mise à jour du patient avec l'ID: {}", id);
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setFirstName(patientDetails.getFirstName());
                    patient.setLastName(patientDetails.getLastName());
                    patient.setBirthDate(patientDetails.getBirthDate());
                    patient.setGender(patientDetails.getGender());
                    patient.setAddress(patientDetails.getAddress());
                    patient.setPhoneNumber(patientDetails.getPhoneNumber());
                    return patientRepository.save(patient);
                });
    }

    /**
     * Supprimer un patient
     */
    public boolean deletePatient(String id) {
        log.info("Suppression du patient avec l'ID: {}", id);
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

