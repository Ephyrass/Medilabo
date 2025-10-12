package com.medilabo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Patient entity representing patient demographic information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
}
