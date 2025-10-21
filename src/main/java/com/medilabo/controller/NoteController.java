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
     * @return List of all notes
     */
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    /**
     * Get notes for a specific patient - Vue historique du patient
     * User Story: En tant que praticien, je veux voir l'historique des informations de mon patient
     * @param patientId The patient identifier
     * @return List of notes for the patient (complete medical history)
     */
    @GetMapping("/patient/{patientId}")
    public List<Note> getNotesByPatientId(@PathVariable String patientId) {
        return noteService.getNotesByPatientId(patientId);
    }

    /**
     * Get a specific note by ID
     * @param id The note identifier
     * @return Note or 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new note - Ajouter une note à l'historique du patient
     * User Story: En tant que praticien, je veux pouvoir ajouter une note d'observation
     * à l'historique du patient afin de vérifier que mes conseils sont suivis d'une séance à l'autre
     * @param note The note to create
     * @return Created note with generated ID and timestamp
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    /**
     * Update an existing note
     * @param id The note identifier
     * @param note The updated note data
     * @return Updated note
     */
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable String id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    /**
     * Delete a note
     * @param id The note identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}
