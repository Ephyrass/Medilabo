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
 * Service pour évaluer le risque de diabète d'un patient
 * Basé sur l'algorithme du Sprint 3
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RiskAssessmentService {

    private final MicroserviceClientService microserviceClient;

    // Liste des déclencheurs de diabète (termes médicaux)
    private static final List<String> DIABETES_TRIGGERS = Arrays.asList(
            "hémoglobine a1c",
            "microalbumine",
            "taille",
            "poids",
            "fumeur",
            "fumeuse",
            "anormal",
            "cholestérol",
            "vertiges",
            "vertige",
            "rechute",
            "réaction",
            "anticorps"
    );

    /**
     * Évalue le risque de diabète pour un patient donné
     */
    public RiskAssessmentDTO assessDiabetesRisk(String patientId) {
        log.info("Assessing diabetes risk for patient ID: {}", patientId);

        // 1. Récupérer les informations du patient
        PatientDTO patient = microserviceClient.getPatient(patientId);
        if (patient == null) {
            throw new RuntimeException("Patient not found with ID: " + patientId);
        }

        // 2. Récupérer les notes du patient
        List<NoteDTO> notes = microserviceClient.getPatientNotes(patientId);

        // 3. Calculer l'âge du patient
        int age = calculateAge(patient.getBirthDate());

        // 4. Compter les déclencheurs dans les notes
        int triggerCount = countTriggers(notes);

        // 5. Déterminer le niveau de risque
        RiskLevel riskLevel = determineRiskLevel(age, patient.getGender(), triggerCount);

        // 6. Construire le message
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
     * Calcule l'âge d'un patient à partir de sa date de naissance
     */
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Compte le nombre de déclencheurs de diabète dans les notes
     */
    private int countTriggers(List<NoteDTO> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0;
        }

        // Concaténer toutes les notes en un seul texte
        StringBuilder allNotes = new StringBuilder();
        for (NoteDTO note : notes) {
            if (note.getContent() != null) {
                allNotes.append(note.getContent().toLowerCase(Locale.FRENCH)).append(" ");
            }
        }

        String fullText = allNotes.toString();
        int count = 0;

        // Compter chaque déclencheur (chaque déclencheur ne compte qu'une fois)
        for (String trigger : DIABETES_TRIGGERS) {
            if (fullText.contains(trigger.toLowerCase())) {
                count++;
                log.debug("Trigger found: {}", trigger);
            }
        }

        return count;
    }

    /**
     * Détermine le niveau de risque selon l'algorithme du Sprint 3
     */
    private RiskLevel determineRiskLevel(int age, String gender, int triggerCount) {
        boolean isMale = "M".equalsIgnoreCase(gender);
        boolean isYoung = age < 30;

        // Règles pour les patients de moins de 30 ans
        if (isYoung) {
            if (isMale) {
                // Homme < 30 ans
                if (triggerCount >= 5) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 3) return RiskLevel.IN_DANGER;
            } else {
                // Femme < 30 ans
                if (triggerCount >= 7) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 4) return RiskLevel.IN_DANGER;
            }
            return RiskLevel.NONE;
        }

        // Règles pour les patients de 30 ans et plus
        if (age >= 30) {
            if (triggerCount >= 8) return RiskLevel.EARLY_ONSET;
            if (triggerCount >= 6) return RiskLevel.IN_DANGER;
            if (triggerCount >= 2) return RiskLevel.BORDERLINE;
        }

        return RiskLevel.NONE;
    }

    /**
     * Construit un message personnalisé pour l'évaluation du risque
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

