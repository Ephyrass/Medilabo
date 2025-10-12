package com.medilabo.controller;

import com.medilabo.model.Note;
import com.medilabo.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for NoteController
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        noteRepository.deleteAll();

        // Add test data
        noteRepository.save(new Note(null, "1", "Dr. Test",
                LocalDateTime.now(), "Test note for patient 1"));
        noteRepository.save(new Note(null, "2", "Dr. Test",
                LocalDateTime.now(), "Test note for patient 2"));
    }

    @Test
    void getAllNotes_shouldReturnNotes() throws Exception {
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getNotesByPatientId_shouldReturnPatientNotes() throws Exception {
        mockMvc.perform(get("/api/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].patientId").value("1"));
    }

    @Test
    void createNote_shouldReturnCreatedNote() throws Exception {
        String noteJson = """
                {
                    "patientId": "3",
                    "doctorName": "Dr. New",
                    "content": "New test note content"
                }
                """;

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.patientId").value("3"))
                .andExpect(jsonPath("$.doctorName").value("Dr. New"))
                .andExpect(jsonPath("$.content").value("New test note content"))
                .andExpect(jsonPath("$.createdDate").exists());
    }
}
