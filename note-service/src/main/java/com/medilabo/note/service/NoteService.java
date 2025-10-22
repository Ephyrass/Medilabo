package com.medilabo.note.service;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;

    public List<Note> getNotesByPatientId(Long patientId) {
        log.info("Fetching notes for patient ID: {}", patientId);
        return noteRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public Note createNote(Long patientId, String content, String authorName) {
        log.info("Creating note for patient ID: {}", patientId);
        Note note = new Note(patientId, content, authorName);
        return noteRepository.save(note);
    }

    public void deleteNote(String noteId) {
        log.info("Deleting note ID: {}", noteId);
        noteRepository.deleteById(noteId);
    }

    public Note updateNote(String noteId, String content) {
        log.info("Updating note ID: {}", noteId);
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(content);
        return noteRepository.save(note);
    }
}

