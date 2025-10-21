package com.medilabo.controller;

import com.medilabo.model.DiabetesReport;
import com.medilabo.service.DiabetesAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for diabetes risk assessment
 * Sprint 3 - User Story: Générer un rapport de diabète
 */
@RestController
@RequestMapping("/api/assess")
@RequiredArgsConstructor
public class DiabetesAssessmentController {

    private final DiabetesAssessmentService assessmentService;

    /**
     * Generate diabetes risk report for a patient
     * User Story: En tant que praticien, je veux pouvoir consulter le risque de diabète
     * pour un patient afin de le prévenir si sa santé est potentiellement en danger
     *
     * @param patientId The patient identifier
     * @return Diabetes report or 404 if patient not found
     */
    @GetMapping("/id/{patientId}")
    public ResponseEntity<DiabetesReport> assessDiabetesRiskById(@PathVariable String patientId) {
        return assessmentService.generateDiabetesReport(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Alternative endpoint using family name
     * @param familyName Patient's family name
     * @return Diabetes report or 404 if patient not found
     */
    @GetMapping("/familyName")
    public ResponseEntity<DiabetesReport> assessDiabetesRiskByFamilyName(
            @RequestParam String familyName) {
        // This would require additional service method to find patient by family name
        // For now, returning not implemented
        return ResponseEntity.status(501).build();
    }
}

