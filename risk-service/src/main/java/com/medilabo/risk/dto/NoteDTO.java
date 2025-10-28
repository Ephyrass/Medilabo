package com.medilabo.risk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO to retrieve medical notes from note-service
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

