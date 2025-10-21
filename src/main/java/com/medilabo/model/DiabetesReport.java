package com.medilabo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Diabetes Risk Report
 * Sprint 3 - User Story: Générer un rapport de diabète
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiabetesReport {
    private String patientId;
    private String patientName;
    private int age;
    private String gender;
    private DiabetesRiskLevel riskLevel;
    private int triggerCount;
}

