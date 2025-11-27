package com.medilabo.risk.service;

import com.medilabo.risk.dto.NoteDTO;
import com.medilabo.risk.dto.PatientDTO;
import com.medilabo.risk.dto.RiskAssessmentDTO;
import com.medilabo.risk.model.RiskLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Service to assess diabetes risk for a patient
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RiskAssessmentService {

    private final MicroserviceClientService microserviceClient;

    // List of diabetes triggers (medical terms)
    private static final List<String> DIABETES_TRIGGERS = Arrays.asList(
            "hemoglobin a1c",
            "microalbumin",
            "height",
            "weight",
            "smoker",
            "abnormal",
            "cholesterol",
            "dizziness",
            "relapse",
            "reaction",
            "antibodies"
    );

    /**
     * Assess diabetes risk for a given patient
     *
     * @param patientId the patient ID
     * @return the risk assessment result
     * @throws RuntimeException if patient not found
     */
    public RiskAssessmentDTO assessDiabetesRisk(String patientId) {
        log.info("Assessing diabetes risk for patient ID: {}", patientId);


        PatientDTO patient = microserviceClient.getPatient(patientId);
        if (patient == null) {
            throw new RuntimeException("Patient not found with ID: " + patientId);
        }


        List<NoteDTO> notes = microserviceClient.getPatientNotes(patientId);

        int age = calculateAge(patient.getBirthDate());

        int triggerCount = countTriggers(notes);

        RiskLevel riskLevel = determineRiskLevel(age, patient.getGender(), triggerCount);


        String message = buildRiskMessage(patient, age, riskLevel, triggerCount);

        log.info("Risk assessment completed for patient {}: {} (triggers: {})",
                patientId, riskLevel, triggerCount);

        return new RiskAssessmentDTO(
                patientId,
                patient.getFirstName() + " " + patient.getLastName(),
                age,
                patient.getGender(),
                riskLevel,
                triggerCount,
                message
        );
    }

    /**
     * Calculate patient age from birth date
     *
     * @param birthDate the birth date
     * @return the age in years
     */
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Count the number of diabetes triggers in notes
     *
     * @param notes the list of medical notes
     * @return the count of unique triggers found
     */
    private int countTriggers(List<NoteDTO> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0;
        }

        StringBuilder allNotes = new StringBuilder();
        for (NoteDTO note : notes) {
            if (note.getContent() != null) {
                allNotes.append(note.getContent().toLowerCase(Locale.ENGLISH)).append(" ");
            }
        }

        String fullText = allNotes.toString();
        int count = 0;

        for (String trigger : DIABETES_TRIGGERS) {
            if (fullText.contains(trigger.toLowerCase())) {
                count++;
                log.debug("Trigger found: {}", trigger);
            }
        }

        return count;
    }

    /**
     * Determine risk level according to Sprint 3 algorithm
     *
     * @param age the patient age
     * @param gender the patient gender (M or F)
     * @param triggerCount the number of triggers found
     * @return the risk level
     */
    private RiskLevel determineRiskLevel(int age, String gender, int triggerCount) {
        boolean isMale = "M".equalsIgnoreCase(gender);
        boolean isYoung = age < 30;

        if (isYoung) {
            if (isMale) {
                if (triggerCount >= 5) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 3) return RiskLevel.IN_DANGER;
            } else {
                if (triggerCount >= 7) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 4) return RiskLevel.IN_DANGER;
            }
            return RiskLevel.NONE;
        }

        // Rules for patients 30 years and older
        if (age >= 30) {
            if (triggerCount >= 8) return RiskLevel.EARLY_ONSET;
            if (triggerCount >= 6) return RiskLevel.IN_DANGER;
            if (triggerCount >= 2) return RiskLevel.BORDERLINE;
        }

        return RiskLevel.NONE;
    }

    /**
     * Build a custom message for the risk assessment
     *
     * @param patient the patient data
     * @param age the patient age
     * @param riskLevel the assessed risk level
     * @param triggerCount the number of triggers found
     * @return the formatted message
     */
    private String buildRiskMessage(PatientDTO patient, int age, RiskLevel riskLevel, int triggerCount) {
        String patientName = patient.getFirstName() + " " + patient.getLastName();

        switch (riskLevel) {
            case NONE:
                return String.format("Patient: %s (age %d) diabetes assessment is %s",
                        patientName, age, riskLevel.getCode());
            case BORDERLINE:
                return String.format("Patient: %s (age %d) diabetes assessment is %s",
                        patientName, age, riskLevel.getCode());
            case IN_DANGER:
                return String.format("Patient: %s (age %d) diabetes assessment is %s",
                        patientName, age, riskLevel.getCode());
            case EARLY_ONSET:
                return String.format("Patient: %s (age %d) diabetes assessment is %s",
                        patientName, age, riskLevel.getCode());
            default:
                return String.format("Patient: %s (age %d) diabetes assessment could not be determined",
                        patientName, age);
        }
    }
}

