package com.medilabo.note.controller;

import com.medilabo.note.dto.NoteRequest;
import com.medilabo.note.model.Note;
import com.medilabo.note.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for NoteController
 */
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    private Note testNote;

    @BeforeEach
    void setUp() {
        testNote = new Note();
        testNote.setId("note1");
        testNote.setPatientId("patient1");
        testNote.setContent("Patient reports feeling well");
        testNote.setAuthorName("Dr. Smith");
        testNote.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetNotesByPatientId_ShouldReturnNotesList() throws Exception {
        // Arrange
        when(noteService.getNotesByPatientId("patient1")).thenReturn(Arrays.asList(testNote));

        // Act & Assert
        mockMvc.perform(get("/api/notes/patient/patient1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patientId").value("patient1"))
                .andExpect(jsonPath("$[0].content").value("Patient reports feeling well"))
                .andExpect(jsonPath("$[0].authorName").value("Dr. Smith"));

        verify(noteService, times(1)).getNotesByPatientId("patient1");
    }

    @Test
    void testCreateNote_WithValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        when(noteService.createNote(eq("patient1"), any(String.class), any(String.class)))
                .thenReturn(testNote);

        String noteJson = """
                {
                    "patientId": "patient1",
                    "content": "Patient reports feeling well",
                    "authorName": "Dr. Smith"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value("patient1"))
                .andExpect(jsonPath("$.content").value("Patient reports feeling well"))
                .andExpect(jsonPath("$.authorName").value("Dr. Smith"));

        verify(noteService, times(1)).createNote(eq("patient1"), any(String.class), any(String.class));
    }

    @Test
    void testUpdateNote_ShouldReturnUpdatedNote() throws Exception {
        // Arrange
        testNote.setContent("Updated content");
        when(noteService.updateNote(eq("note1"), any(String.class))).thenReturn(testNote);

        String noteJson = """
                {
                    "patientId": "patient1",
                    "content": "Updated content",
                    "authorName": "Dr. Smith"
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/api/notes/note1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));

        verify(noteService, times(1)).updateNote(eq("note1"), any(String.class));
    }

    @Test
    void testDeleteNote_ShouldReturn204() throws Exception {
        // Arrange
        doNothing().when(noteService).deleteNote("note1");

        // Act & Assert
        mockMvc.perform(delete("/api/notes/note1"))
                .andExpect(status().isNoContent());

        verify(noteService, times(1)).deleteNote("note1");
    }
}

