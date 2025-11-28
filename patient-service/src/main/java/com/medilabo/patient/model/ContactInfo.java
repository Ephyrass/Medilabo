package com.medilabo.patient.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ContactInfo entity
 * Separated from Patient for 3NF normalization
 * Contact information forms a separate relation with its own primary key
 */
@Entity
@Table(name = "contact_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String phoneNumber;

    @OneToOne(mappedBy = "contactInfo")
    @JsonBackReference
    private Patient patient;
}

