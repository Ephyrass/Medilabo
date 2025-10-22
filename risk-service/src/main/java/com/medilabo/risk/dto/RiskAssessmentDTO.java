package com.medilabo.risk.dto;

import com.medilabo.risk.model.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour retourner l'évaluation du risque de diabète
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessmentDTO {
    private String patientId;
    private String patientName;
    private int age;
    private String gender;
    private RiskLevel riskLevel;
    private int triggerCount;
    private String message;
}

