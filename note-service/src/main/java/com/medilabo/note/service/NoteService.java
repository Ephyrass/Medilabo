package com.medilabo.note.service;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing medical notes
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;

    /**
     * Get all notes for a patient
     *
     * @param patientId the patient ID
     * @return list of notes sorted by creation date (newest first)
     */
    public List<Note> getNotesByPatientId(String patientId) {
        log.info("Fetching notes for patient ID: {}", patientId);
        return noteRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    /**
     * Create a new note
     *
     * @param patientId the patient ID
     * @param content the note content
     * @param authorName the author name
     * @return the created note
     */
    public Note createNote(String patientId, String content, String authorName) {
        log.info("Creating note for patient ID: {}", patientId);
        Note note = new Note(patientId, content, authorName);
        return noteRepository.save(note);
    }

    /**
     * Delete a note
     *
     * @param noteId the note ID
     */
    public void deleteNote(String noteId) {
        log.info("Deleting note ID: {}", noteId);
        noteRepository.deleteById(noteId);
    }

    /**
     * Update a note's content
     *
     * @param noteId the note ID
     * @param content the new content
     * @return the updated note
     * @throws RuntimeException if note not found
     */
    public Note updateNote(String noteId, String content) {
        log.info("Updating note ID: {}", noteId);
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(content);
        return noteRepository.save(note);
    }
}

