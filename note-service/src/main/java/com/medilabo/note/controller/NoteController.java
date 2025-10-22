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

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Note>> getNotesByPatientId(@PathVariable Long patientId) {
        log.info("GET /api/notes/patient/{}", patientId);
        List<Note> notes = noteService.getNotesByPatientId(patientId);
        return ResponseEntity.ok(notes);
    }

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

    @PutMapping("/{noteId}")
    public ResponseEntity<Note> updateNote(
            @PathVariable String noteId,
            @RequestBody NoteRequest request) {
        log.info("PUT /api/notes/{}", noteId);
        Note note = noteService.updateNote(noteId, request.getContent());
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable String noteId) {
        log.info("DELETE /api/notes/{}", noteId);
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }
}

