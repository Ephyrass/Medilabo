package com.medilabo.risk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour récupérer les notes médicales depuis le note-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private String id;
    private String patientId;
    private String content;
    private LocalDateTime createdAt;
    private String authorName;
}

