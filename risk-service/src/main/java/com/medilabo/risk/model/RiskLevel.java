package com.medilabo.risk.model;

/**
 * Diabetes risk level enumeration
 */
public enum RiskLevel {
    NONE("None", "No risk"),
    BORDERLINE("Borderline", "Borderline risk"),
    IN_DANGER("In Danger", "In danger"),
    EARLY_ONSET("Early onset", "Early onset");

    private final String code;
    private final String description;

    /**
     * Constructor for RiskLevel enum
     *
     * @param code the risk level code
     * @param description the risk level description
     */
    RiskLevel(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Get the risk level code
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the risk level description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}

