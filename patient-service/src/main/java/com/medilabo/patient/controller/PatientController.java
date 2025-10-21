package com.medilabo.patient.controller;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller pour la gestion des patients - Sprint 1
 * Endpoints REST pour gérer le dossier du patient avec MongoDB
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    /**
     * GET /api/patients - Vue des informations personnelles des patients
     * User Story Sprint 1: Voir les informations personnelles de mes patients
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        log.info("GET /api/patients - Récupération de tous les patients");
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * GET /api/patients/{id} - Vue des informations d'un patient spécifique
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        log.info("GET /api/patients/{} - Récupération d'un patient", id);
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/patients - Ajouter des informations personnelles des patients
     * User Story Sprint 1: Ajouter des informations personnelles aux patients
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody Patient patient) {
        log.info("POST /api/patients - Ajout d'un nouveau patient: {} {}",
                patient.getFirstName(), patient.getLastName());
        Patient savedPatient = patientService.addPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    /**
     * PUT /api/patients/{id} - Mise à jour des informations personnelles
     * User Story Sprint 1: Mettre à jour les informations personnelles de mes patients
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable String id,
            @Valid @RequestBody Patient patient) {
        log.info("PUT /api/patients/{} - Mise à jour d'un patient", id);
        return patientService.updatePatient(id, patient)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/patients/{id} - Supprimer un patient
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        log.info("DELETE /api/patients/{} - Suppression d'un patient", id);
        if (patientService.deletePatient(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

