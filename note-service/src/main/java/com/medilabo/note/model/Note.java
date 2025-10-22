package com.medilabo.note.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    private String id;

    private Long patientId;

    private String content;

    private LocalDateTime createdAt;

    private String authorName; // Nom du praticien

    public Note(Long patientId, String content, String authorName) {
        this.patientId = patientId;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = LocalDateTime.now();
    }
}

