package com.medilabo.controller;

import com.medilabo.model.Patient;
import com.medilabo.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for patient-related endpoints
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;

    /**
     * Get all patients - Vue des informations personnelles des patients
     * @return List of all patients
     */
    @GetMapping
    public List<Patient> getAll() {
        return patientService.getAllPatients();
    }

    /**
     * Get patient by ID - Vue des informations personnelles d'un patient
     * @param id The patient identifier
     * @return Patient or 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable String id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add a new patient - Ajouter des informations personnelles des patients
     * @param patient The patient to add
     * @return Created patient
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    /**
     * Update patient information - Mise à jour des informations personnelles
     * @param id The patient identifier
     * @param patient The updated patient information
     * @return Updated patient or 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
