package com.medilabo.model;

/**
 * Diabetes risk levels for patient assessment
 * Sprint 3 - User Story: Générer un rapport de diabète
 */
public enum DiabetesRiskLevel {
    /**
     * None - No diabetes risk
     * Le dossier du patient ne contient aucune note du médecin contenant les déclencheurs
     */
    NONE("None"),

    /**
     * Borderline - Limited risk
     * Le dossier du patient contient entre deux et cinq déclencheurs et le patient est âgé de plus de 30 ans
     */
    BORDERLINE("Borderline"),

    /**
     * In Danger - Patient is in danger
     * Dépend de l'âge et du sexe du patient:
     * - Homme < 30 ans: 3 déclencheurs
     * - Femme < 30 ans: 4 déclencheurs
     * - Patient > 30 ans: 6 ou 7 déclencheurs
     */
    IN_DANGER("In Danger"),

    /**
     * Early Onset - Early onset diabetes
     * Dépend de l'âge et du sexe:
     * - Homme < 30 ans: 5+ déclencheurs
     * - Femme < 30 ans: 7+ déclencheurs
     * - Patient > 30 ans: 8+ déclencheurs
     */
    EARLY_ONSET("Early onset");

    private final String displayName;

    DiabetesRiskLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

