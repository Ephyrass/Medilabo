package com.medilabo.service;

import com.medilabo.model.DiabetesReport;
import com.medilabo.model.DiabetesRiskLevel;
import com.medilabo.model.Note;
import com.medilabo.model.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service for diabetes risk assessment
 * Sprint 3 - User Story: Générer un rapport de diabète
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DiabetesAssessmentService {

    private final IPatientService patientService;
    private final INoteService noteService;

    /**
     * Medical trigger terms to search for in doctor's notes
     * Termes déclencheurs à rechercher dans les notes du prestataire de santé
     */
    private static final List<String> TRIGGER_TERMS = Arrays.asList(
            "hémoglobine a1c",
            "microalbumine",
            "taille",
            "poids",
            "fumeur",
            "fumeuse",
            "anormal",
            "cholestérol",
            "vertige",
            "vertiges",
            "rechute",
            "réaction",
            "anticorps"
    );

    /**
     * Generate diabetes risk report for a patient
     * @param patientId The patient identifier
     * @return Optional containing the diabetes report if patient exists
     */
    public Optional<DiabetesReport> generateDiabetesReport(String patientId) {
        log.info("Generating diabetes risk report for patient: {}", patientId);

        // Get patient information
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            log.warn("Patient not found: {}", patientId);
            return Optional.empty();
        }

        Patient patient = patientOpt.get();

        // Calculate age
        int age = calculateAge(patient.getBirthDate());

        // Get all notes for the patient
        List<Note> notes = noteService.getNotesByPatientId(patientId);

        // Count trigger terms in notes
        int triggerCount = countTriggerTerms(notes);

        // Assess risk level based on business rules
        DiabetesRiskLevel riskLevel = assessRiskLevel(age, patient.getGender(), triggerCount);

        // Create report
        DiabetesReport report = new DiabetesReport(
                patientId,
                patient.getFirstName() + " " + patient.getLastName(),
                age,
                patient.getGender(),
                riskLevel,
                triggerCount
        );

        log.info("Diabetes report generated - Patient: {}, Age: {}, Gender: {}, Triggers: {}, Risk: {}",
                report.getPatientName(), age, patient.getGender(), triggerCount, riskLevel);

        return Optional.of(report);
    }

    /**
     * Calculate patient's age from birth date
     */
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Count trigger terms found in patient's medical notes
     * Case-insensitive search, counts each term only once even if it appears multiple times
     */
    private int countTriggerTerms(List<Note> notes) {
        // Concatenate all note contents
        String allNotes = notes.stream()
                .map(Note::getContent)
                .map(String::toLowerCase)
                .reduce("", (a, b) -> a + " " + b);

        // Count unique trigger terms found
        long count = TRIGGER_TERMS.stream()
                .filter(allNotes::contains)
                .count();

        return (int) count;
    }

    /**
     * Assess diabetes risk level based on business rules
     *
     * Rules:
     * - NONE: No trigger terms in patient records
     * - BORDERLINE: 2-5 triggers AND age > 30
     * - IN_DANGER:
     *   * Male < 30: 3 triggers
     *   * Female < 30: 4 triggers
     *   * Age > 30: 6-7 triggers
     * - EARLY_ONSET:
     *   * Male < 30: 5+ triggers
     *   * Female < 30: 7+ triggers
     *   * Age > 30: 8+ triggers
     */
    private DiabetesRiskLevel assessRiskLevel(int age, String gender, int triggerCount) {
        boolean isMale = "M".equalsIgnoreCase(gender);
        boolean isYoung = age < 30;

        // No triggers = No risk
        if (triggerCount == 0) {
            return DiabetesRiskLevel.NONE;
        }

        // Young patients (< 30 years)
        if (isYoung) {
            if (isMale) {
                // Male < 30
                if (triggerCount >= 5) {
                    return DiabetesRiskLevel.EARLY_ONSET;
                } else if (triggerCount >= 3) {
                    return DiabetesRiskLevel.IN_DANGER;
                }
            } else {
                // Female < 30
                if (triggerCount >= 7) {
                    return DiabetesRiskLevel.EARLY_ONSET;
                } else if (triggerCount >= 4) {
                    return DiabetesRiskLevel.IN_DANGER;
                }
            }
            // Young patients with 1-2 triggers (male) or 1-3 triggers (female) = None
            return DiabetesRiskLevel.NONE;
        }

        // Older patients (>= 30 years)
        if (triggerCount >= 8) {
            return DiabetesRiskLevel.EARLY_ONSET;
        } else if (triggerCount >= 6) {
            return DiabetesRiskLevel.IN_DANGER;
        } else if (triggerCount >= 2) {
            return DiabetesRiskLevel.BORDERLINE;
        }

        return DiabetesRiskLevel.NONE;
    }
}

