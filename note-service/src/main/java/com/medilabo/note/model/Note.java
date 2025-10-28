package com.medilabo.note.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Note entity
 * Represents a medical note for a patient
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    private String patientId;

    private String content;

    private LocalDateTime createdAt;

    private String authorName; // Practitioner name

    /**
     * Constructor with required fields
     *
     * @param patientId the patient ID
     * @param content the note content
     * @param authorName the author name
     */
    public Note(String patientId, String content, String authorName) {
        this.patientId = patientId;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = LocalDateTime.now();
    }
}

