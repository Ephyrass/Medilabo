package com.medilabo.note.config;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Initialisation des données de test - Sprint 2
 * Charge les notes pour les 4 cas de test patients
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;

    @Override
    public void run(String... args) {
        if (noteRepository.count() == 0) {
            log.info("Initialisation des notes de test pour les 4 patients");

            // Notes pour Patient 1: TestNone (ID=1) - Aucun risque
            createNote("1", "Le patient déclare qu'il se sent très bien. Poids égal ou inférieur au poids recommandé", "Dr. Smith", LocalDateTime.now().minusMonths(6));

            // Notes pour Patient 2: TestBorderline (ID=2) - Risque limité
            createNote("2", "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement", "Dr. Johnson", LocalDateTime.now().minusMonths(12));
            createNote("2", "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale", "Dr. Johnson", LocalDateTime.now().minusMonths(6));

            // Notes pour Patient 3: TestInDanger (ID=3) - En danger
            createNote("3", "Le patient déclare qu'il fume depuis peu", "Dr. Williams", LocalDateTime.now().minusMonths(18));
            createNote("3", "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d'apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé", "Dr. Williams", LocalDateTime.now().minusMonths(12));

            // Notes pour Patient 4: TestEarlyOnset (ID=4) - Apparition précoce
            createNote("4", "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d'être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments", "Dr. Brown", LocalDateTime.now().minusMonths(24));
            createNote("4", "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps", "Dr. Brown", LocalDateTime.now().minusMonths(18));
            createNote("4", "Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé", "Dr. Brown", LocalDateTime.now().minusMonths(12));
            createNote("4", "Taille, Poids, Cholestérol, Vertige et Réaction", "Dr. Brown", LocalDateTime.now().minusMonths(6));

            log.info("Données de test initialisées: {} notes créées pour 4 patients", noteRepository.count());
        } else {
            log.info("Base de données déjà initialisée avec {} notes", noteRepository.count());
        }
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

