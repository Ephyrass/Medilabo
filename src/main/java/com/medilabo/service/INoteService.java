package com.medilabo.service;

import com.medilabo.model.Note;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for medical note management operations
 */
public interface INoteService {

    /**
     * Retrieves all notes from the system
     * @return List of all notes
     */
    List<Note> getAllNotes();

    /**
     * Retrieves notes for a specific patient
     * @param patientId The patient identifier
     * @return List of notes for the patient
     */
    List<Note> getNotesByPatientId(String patientId);

    /**
     * Retrieves a specific note by its ID
     * @param id The note identifier
     * @return Optional containing the note if found
     */
    Optional<Note> getNoteById(String id);

    /**
     * Creates a new note
     * @param note The note to create
     * @return The created note with generated ID
     */
    Note createNote(Note note);

    /**
     * Updates an existing note
     * @param id The note identifier
     * @param note The updated note data
     * @return The updated note
     */
    Note updateNote(String id, Note note);

    /**
     * Deletes a note
     * @param id The note identifier
     */
    void deleteNote(String id);
}

