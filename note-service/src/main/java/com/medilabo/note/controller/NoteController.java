package com.medilabo.note.controller;

import com.medilabo.note.dto.NoteRequest;
import com.medilabo.note.model.Note;
import com.medilabo.note.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for medical notes management - Sprint 2
 * REST endpoints to manage patient medical notes with MongoDB
 */
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    /**
     * GET /api/notes/patient/{patientId} - Get all notes for a patient
     *
     * @param patientId the patient ID
     * @return list of notes for the patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Note>> getNotesByPatientId(@PathVariable String patientId) {
        log.info("GET /api/notes/patient/{}", patientId);
        List<Note> notes = noteService.getNotesByPatientId(patientId);
        return ResponseEntity.ok(notes);
    }

    /**
     * POST /api/notes - Create a new note
     *
     * @param request the note request containing patient ID, content and author name
     * @return the created note
     */
    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody NoteRequest request) {
        log.info("POST /api/notes - Creating note for patient {}", request.getPatientId());
        Note note = noteService.createNote(
                request.getPatientId(),
                request.getContent(),
                request.getAuthorName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    /**
     * PUT /api/notes/{noteId} - Update an existing note
     *
     * @param noteId the note ID
     * @param request the note request with updated content
     * @return the updated note
     */
    @PutMapping("/{noteId}")
    public ResponseEntity<Note> updateNote(
            @PathVariable String noteId,
            @RequestBody NoteRequest request) {
        log.info("PUT /api/notes/{}", noteId);
        Note note = noteService.updateNote(noteId, request.getContent());
        return ResponseEntity.ok(note);
    }

    /**
     * DELETE /api/notes/{noteId} - Delete a note
     *
     * @param noteId the note ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable String noteId) {
        log.info("DELETE /api/notes/{}", noteId);
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }
}

