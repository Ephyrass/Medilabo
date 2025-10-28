package com.medilabo.note.service;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NoteService
 */
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
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
    void testGetNotesByPatientId_ShouldReturnNotesList() {
        // Arrange
        List<Note> notes = Arrays.asList(testNote, new Note());
        when(noteRepository.findByPatientIdOrderByCreatedAtDesc("patient1")).thenReturn(notes);

        // Act
        List<Note> result = noteService.getNotesByPatientId("patient1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(noteRepository, times(1)).findByPatientIdOrderByCreatedAtDesc("patient1");
    }

    @Test
    void testGetNotesByPatientId_WhenNoNotes_ShouldReturnEmptyList() {
        // Arrange
        when(noteRepository.findByPatientIdOrderByCreatedAtDesc("patient999")).thenReturn(List.of());

        // Act
        List<Note> result = noteService.getNotesByPatientId("patient999");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(noteRepository, times(1)).findByPatientIdOrderByCreatedAtDesc("patient999");
    }

    @Test
    void testCreateNote_ShouldSaveAndReturnNote() {
        // Arrange
        Note newNote = new Note("patient1", "Test content", "Dr. Smith");
        newNote.setId("note2");
        when(noteRepository.save(any(Note.class))).thenReturn(newNote);

        // Act
        Note result = noteService.createNote("patient1", "Test content", "Dr. Smith");

        // Assert
        assertNotNull(result);
        assertEquals("patient1", result.getPatientId());
        assertEquals("Test content", result.getContent());
        assertEquals("Dr. Smith", result.getAuthorName());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testUpdateNote_WhenNoteExists_ShouldUpdateAndReturn() {
        // Arrange
        when(noteRepository.findById("note1")).thenReturn(Optional.of(testNote));
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        // Act
        Note result = noteService.updateNote("note1", "Updated content");

        // Assert
        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(noteRepository, times(1)).findById("note1");
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testUpdateNote_WhenNoteDoesNotExist_ShouldThrowException() {
        // Arrange
        when(noteRepository.findById("note999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            noteService.updateNote("note999", "Updated content");
        });

        verify(noteRepository, times(1)).findById("note999");
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void testDeleteNote_ShouldCallRepository() {
        // Arrange
        doNothing().when(noteRepository).deleteById("note1");

        // Act
        noteService.deleteNote("note1");

        // Assert
        verify(noteRepository, times(1)).deleteById("note1");
    }
}

