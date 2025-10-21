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
        noteRepository.save(new Note(null, "1", "TestNone",
                LocalDateTime.now(), "Test note for patient 1"));
        noteRepository.save(new Note(null, "2", "TestBorderline",
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

    /**
     * Test User Story: Ajouter une note à l'historique du patient
     * En tant que praticien, je veux pouvoir ajouter une note d'observation
     */
    @Test
    void createNote_shouldReturnCreatedNote() throws Exception {
        String noteJson = """
                {
                    "patientId": "3",
                    "patientName": "TestInDanger",
                    "content": "Patient reports improvement after following treatment plan. Blood pressure stable."
                }
                """;

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.patientId").value("3"))
                .andExpect(jsonPath("$.patientName").value("TestInDanger"))
                .andExpect(jsonPath("$.content").value("Patient reports improvement after following treatment plan. Blood pressure stable."))
                .andExpect(jsonPath("$.createdDate").exists());
    }

    /**
     * Test User Story: Vue historique du patient
     * En tant que praticien, je veux voir l'historique des informations de mon patient
     */
    @Test
    void getNotesByPatientId_shouldReturnCompletePatientHistory() throws Exception {
        // Add multiple notes for same patient to test history
        noteRepository.save(new Note(null, "1", "TestNone",
                LocalDateTime.now().minusDays(5), "First consultation - routine checkup"));
        noteRepository.save(new Note(null, "1", "TestNone",
                LocalDateTime.now().minusDays(2), "Follow-up - patient shows improvement"));

        mockMvc.perform(get("/api/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3)) // 1 from setUp + 2 added here
                .andExpect(jsonPath("$[*].patientId").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is("1"))))
                .andExpect(jsonPath("$[*].content").exists())
                .andExpect(jsonPath("$[*].createdDate").exists());
    }

    @Test
    void getNotesByPatientId_shouldReturnEmptyArrayForPatientWithoutNotes() throws Exception {
        mockMvc.perform(get("/api/notes/patient/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getNoteById_shouldReturnSpecificNote() throws Exception {
        Note savedNote = noteRepository.save(new Note(null, "1", "TestNone",
                LocalDateTime.now(), "Specific test note"));

        mockMvc.perform(get("/api/notes/" + savedNote.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedNote.getId()))
                .andExpect(jsonPath("$.content").value("Specific test note"));
    }

    @Test
    void updateNote_shouldUpdateExistingNote() throws Exception {
        Note savedNote = noteRepository.save(new Note(null, "1", "TestNone",
                LocalDateTime.now(), "Original content"));

        String updatedNoteJson = """
                {
                    "patientName": "TestNone",
                    "content": "Updated content after review"
                }
                """;

        mockMvc.perform(put("/api/notes/" + savedNote.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedNoteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedNote.getId()))
                .andExpect(jsonPath("$.content").value("Updated content after review"));
    }

    @Test
    void deleteNote_shouldRemoveNote() throws Exception {
        Note savedNote = noteRepository.save(new Note(null, "1", "TestNone",
                LocalDateTime.now(), "Note to be deleted"));

        mockMvc.perform(delete("/api/notes/" + savedNote.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/notes/" + savedNote.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
