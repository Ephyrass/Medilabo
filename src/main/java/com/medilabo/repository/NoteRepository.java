package com.medilabo.repository;

import com.medilabo.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for Note entities
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Find all notes for a specific patient
     * @param patientId The patient identifier
     * @return List of notes for the patient
     */
    List<Note> findByPatientId(String patientId);
}

