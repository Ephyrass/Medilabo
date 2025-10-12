package com.medilabo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Medical note entity representing doctor's notes for patients
 * Stored in MongoDB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    private String patientId;

    private String doctorName;

    private LocalDateTime createdDate;

    private String content;
}

