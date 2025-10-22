package com.medilabo.risk.model;

/**
 * Énumération des niveaux de risque de diabète
 * Basé sur les exigences du Sprint 3
 */
public enum RiskLevel {
    NONE("None", "Aucun risque"),
    BORDERLINE("Borderline", "Risque limité"),
    IN_DANGER("In Danger", "Danger"),
    EARLY_ONSET("Early onset", "Apparition précoce");

    private final String code;
    private final String description;

    RiskLevel(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

