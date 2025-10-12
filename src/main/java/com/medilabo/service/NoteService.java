package com.medilabo.service;

import com.medilabo.model.Note;
import com.medilabo.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of note service using MongoDB repository
 */
@Service
@RequiredArgsConstructor
public class NoteService implements INoteService {

    private final NoteRepository noteRepository;

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> getNotesByPatientId(String patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    @Override
    public Optional<Note> getNoteById(String id) {
        return noteRepository.findById(id);
    }

    @Override
    public Note createNote(Note note) {
        note.setCreatedDate(LocalDateTime.now());
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(String id, Note note) {
        return noteRepository.findById(id)
                .map(existingNote -> {
                    existingNote.setContent(note.getContent());
                    existingNote.setDoctorName(note.getDoctorName());
                    return noteRepository.save(existingNote);
                })
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
    }

    @Override
    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}

