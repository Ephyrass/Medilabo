package com.medilabo.note.repository;

import com.medilabo.note.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for medical notes
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Find all notes for a patient ordered by creation date descending
     *
     * @param patientId the patient ID
     * @return list of notes sorted by creation date (newest first)
     */
    List<Note> findByPatientIdOrderByCreatedAtDesc(String patientId);
}

