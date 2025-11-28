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
 * REST Controller for patient management
 * REST endpoints to manage patient records with MongoDB
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    /**
     * GET /api/patients - View personal information of patients
     * User Story Sprint 1: View personal information of my patients
     *
     * @return list of all patients
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        log.info("GET /api/patients - Fetching all patients");
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * GET /api/patients/{id} - View information of a specific patient
     *
     * @param id the patient ID
     * @return the patient if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        log.info("GET /api/patients/{} - Fetching patient", id);
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/patients - Add personal information of patients
     * User Story Sprint 1: Add personal information to patients
     *
     * @param patient the patient to add
     * @return the created patient
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody Patient patient) {
        log.info("POST /api/patients - Adding new patient: {} {}",
                patient.getFirstName(), patient.getLastName());
        Patient savedPatient = patientService.addPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    /**
     * PUT /api/patients/{id} - Update personal information
     * User Story Sprint 1: Update personal information of my patients
     *
     * @param id the patient ID
     * @param patient the updated patient information
     * @return the updated patient if found, 404 otherwise
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody Patient patient) {
        log.info("PUT /api/patients/{} - Updating patient", id);
        return patientService.updatePatient(id, patient)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/patients/{id} - Delete a patient
     *
     * @param id the patient ID
     * @return 204 if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.info("DELETE /api/patients/{} - Deleting patient", id);
        if (patientService.deletePatient(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

