package com.medilabo.controller;

import com.medilabo.model.Note;
import com.medilabo.service.INoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for medical note-related endpoints
 */
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final INoteService noteService;

    /**
     * Get all notes
     * @return List of all notes with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    /**
     * Get notes for a specific patient
     * @param patientId The patient identifier
     * @return List of notes for the patient with HTTP 200 status
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Note>> getNotesByPatientId(@PathVariable String patientId) {
        return ResponseEntity.ok(noteService.getNotesByPatientId(patientId));
    }

    /**
     * Get a specific note by ID
     * @param id The note identifier
     * @return The note with HTTP 200 status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new note
     * @param note The note to create
     * @return The created note with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note createdNote = noteService.createNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    /**
     * Update an existing note
     * @param id The note identifier
     * @param note The updated note data
     * @return The updated note with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable String id, @RequestBody Note note) {
        Note updatedNote = noteService.updateNote(id, note);
        return ResponseEntity.ok(updatedNote);
    }

    /**
     * Delete a note
     * @param id The note identifier
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
