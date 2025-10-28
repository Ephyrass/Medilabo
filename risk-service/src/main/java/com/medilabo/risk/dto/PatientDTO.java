package com.medilabo.risk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO to retrieve patient information from patient-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private String id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String gender; // M or F

    private String address;

    private String phoneNumber;
}

