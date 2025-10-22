package com.medilabo.risk.controller;

import com.medilabo.risk.dto.RiskAssessmentDTO;
import com.medilabo.risk.service.RiskAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for diabetes risk assessment
 */
@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    /**
     * Assess diabetes risk for a specific patient
     *
     * @param patientId the patient ID
     * @return the risk assessment result
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<RiskAssessmentDTO> assessRisk(@PathVariable String patientId) {
        log.info("Received request to assess risk for patient: {}", patientId);

        try {
            RiskAssessmentDTO assessment = riskAssessmentService.assessDiabetesRisk(patientId);
            return ResponseEntity.ok(assessment);
        } catch (RuntimeException e) {
            log.error("Error assessing risk for patient {}: {}", patientId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Risk Service is running");
    }
}

