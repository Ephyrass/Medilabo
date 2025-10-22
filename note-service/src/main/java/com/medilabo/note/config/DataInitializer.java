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
 * Initialisation des données de test - Sprint 2
 * Charge les notes pour les 4 cas de test patients
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
            log.info("Initialisation des notes de test pour les 4 patients");

            // Récupérer les IDs réels des patients depuis la base de données
            String testNoneId = getPatientIdByLastName("TestNone");
            String testBorderlineId = getPatientIdByLastName("TestBorderline");
            String testInDangerId = getPatientIdByLastName("TestInDanger");
            String testEarlyOnsetId = getPatientIdByLastName("TestEarlyOnset");

            if (testNoneId != null) {
                // Notes pour Patient 1: TestNone - Aucun risque
                createNote(testNoneId, "Le patient déclare qu'il se sent très bien. Poids égal ou inférieur au poids recommandé", "Dr. Smith", LocalDateTime.now().minusMonths(6));
            }

            if (testBorderlineId != null) {
                // Notes pour Patient 2: TestBorderline - Risque limité
                createNote(testBorderlineId, "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement", "Dr. Johnson", LocalDateTime.now().minusMonths(12));
                createNote(testBorderlineId, "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale", "Dr. Johnson", LocalDateTime.now().minusMonths(6));
            }

            if (testInDangerId != null) {
                // Notes pour Patient 3: TestInDanger - En danger
                createNote(testInDangerId, "Le patient déclare qu'il fume depuis peu", "Dr. Williams", LocalDateTime.now().minusMonths(18));
                createNote(testInDangerId, "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d'apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé", "Dr. Williams", LocalDateTime.now().minusMonths(12));
            }

            if (testEarlyOnsetId != null) {
                // Notes pour Patient 4: TestEarlyOnset - Apparition précoce
                createNote(testEarlyOnsetId, "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d'être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments", "Dr. Brown", LocalDateTime.now().minusMonths(24));
                createNote(testEarlyOnsetId, "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps", "Dr. Brown", LocalDateTime.now().minusMonths(18));
                createNote(testEarlyOnsetId, "Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé", "Dr. Brown", LocalDateTime.now().minusMonths(12));
                createNote(testEarlyOnsetId, "Taille, Poids, Cholestérol, Vertige et Réaction", "Dr. Brown", LocalDateTime.now().minusMonths(6));
            }

            log.info("Données de test initialisées: {} notes créées pour les patients de test", noteRepository.count());
        } else {
            log.info("Base de données déjà initialisée avec {} notes", noteRepository.count());
        }
    }

    private String getPatientIdByLastName(String lastName) {
        try {
            Query query = new Query(Criteria.where("lastName").is(lastName));
            Map<String, Object> patient = mongoTemplate.findOne(query, Map.class, "patients");
            if (patient != null && patient.containsKey("_id")) {
                String id = patient.get("_id").toString();
                log.debug("Patient {} trouvé avec ID: {}", lastName, id);
                return id;
            }
        } catch (Exception e) {
            log.warn("Patient {} non trouvé: {}", lastName, e.getMessage());
        }
        return null;
    }

    private void createNote(String patientId, String content, String authorName, LocalDateTime createdAt) {
        Note note = new Note();
        note.setPatientId(patientId);
        note.setContent(content);
        note.setAuthorName(authorName);
        note.setCreatedAt(createdAt);
        noteRepository.save(note);
        log.debug("Note créée pour le patient {}: {}", patientId, content.substring(0, Math.min(50, content.length())) + "...");
    }
}

