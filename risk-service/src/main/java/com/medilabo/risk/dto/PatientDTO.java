package com.medilabo.risk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO pour récupérer les informations du patient depuis le patient-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender; // M ou F
    private String address;
    private String phoneNumber;
}

