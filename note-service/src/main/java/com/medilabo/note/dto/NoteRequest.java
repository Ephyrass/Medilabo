package com.medilabo.note.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a note
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

    @NotNull(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String authorName;
}

