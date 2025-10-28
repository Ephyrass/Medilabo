package com.medilabo.note.config;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Test data initialization
 * Loads notes for the 4 test patients
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        if (noteRepository.count() == 0) {
            log.info("Initializing test notes for the 4 patients");

            // Retrieve actual patient IDs from the database
            String testNoneId = getPatientIdByLastName("TestNone");
            String testBorderlineId = getPatientIdByLastName("TestBorderline");
            String testInDangerId = getPatientIdByLastName("TestInDanger");
            String testEarlyOnsetId = getPatientIdByLastName("TestEarlyOnset");

            if (testNoneId != null) {
                // Notes for Patient 1: TestNone - No risk
                createNote(testNoneId, "Patient states that they are feeling terrific Weight at or below recommended", "Dr. Smith", LocalDateTime.now().minusMonths(6));
            }

            if (testBorderlineId != null) {
                // Notes for Patient 2: TestBorderline - Borderline risk
                createNote(testBorderlineId, "Patient states that they are feeling a great deal of stress at work Patient also complains that their hearing seems abnormal as of late", "Dr. Johnson", LocalDateTime.now().minusMonths(12));
                createNote(testBorderlineId, "Patient states that they had a reaction to medication within last 3 months Patient also complains that their hearing continues to be abnormal", "Dr. Johnson", LocalDateTime.now().minusMonths(6));
            }

            if (testInDangerId != null) {
                // Notes for Patient 3: TestInDanger - In Danger
                createNote(testInDangerId, "Patient states that they are short term Smoker", "Dr. Williams", LocalDateTime.now().minusMonths(18));
                createNote(testInDangerId, "Patient states that they quit within last year Patient also complains that they are experiencing abnormal breathing spells Lab reports Cholesterol LDL high", "Dr. Williams", LocalDateTime.now().minusMonths(12));
            }

            if (testEarlyOnsetId != null) {
                // Notes for Patient 4: TestEarlyOnset - Early onset
                createNote(testEarlyOnsetId, "Patient states that walking up stairs has become difficult Patient also complains that they are having shortness of breath Lab results indicate Antibodies present elevated Reaction to medication", "Dr. Brown", LocalDateTime.now().minusMonths(24));
                createNote(testEarlyOnsetId, "Patient states that they are experiencing back pain when seated for a long time", "Dr. Brown", LocalDateTime.now().minusMonths(18));
                createNote(testEarlyOnsetId, "Patient states that they are a short term Smoker Hemoglobin A1C above recommended level", "Dr. Brown", LocalDateTime.now().minusMonths(12));
                createNote(testEarlyOnsetId, "Body Height, Body Weight, Cholesterol, Dizziness and Reaction", "Dr. Brown", LocalDateTime.now().minusMonths(6));
            }

            log.info("Test data initialized: {} notes created for test patients", noteRepository.count());
        } else {
            log.info("Database already initialized with {} notes", noteRepository.count());
        }
    }

    /**
     * Get patient ID by last name from MongoDB
     *
     * @param lastName the patient's last name
     * @return the patient ID or null if not found
     */
    private String getPatientIdByLastName(String lastName) {
        try {
            Query query = new Query(Criteria.where("lastName").is(lastName));
            Map<String, Object> patient = mongoTemplate.findOne(query, Map.class, "patients");
            if (patient != null && patient.containsKey("_id")) {
                String id = patient.get("_id").toString();
                log.debug("Patient {} found with ID: {}", lastName, id);
                return id;
            }
        } catch (Exception e) {
            log.warn("Patient {} not found: {}", lastName, e.getMessage());
        }
        return null;
    }

    /**
     * Create and save a note
     *
     * @param patientId the patient ID
     * @param content the note content
     * @param authorName the author name
     * @param createdAt the creation date
     */
    private void createNote(String patientId, String content, String authorName, LocalDateTime createdAt) {
        Note note = new Note();
        note.setPatientId(patientId);
        note.setContent(content);
        note.setAuthorName(authorName);
        note.setCreatedAt(createdAt);
        noteRepository.save(note);
        log.debug("Note created for patient {}: {}", patientId, content.substring(0, Math.min(50, content.length())) + "...");
    }
}

