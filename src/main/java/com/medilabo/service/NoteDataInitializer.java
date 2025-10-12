package com.medilabo.service;

import com.medilabo.model.Note;
import com.medilabo.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Data initializer to load test notes for Sprint 2
 * Runs automatically on application startup (only in non-test profiles)
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class NoteDataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;

    @Override
    public void run(String... args) {
        // Clear existing data
        noteRepository.deleteAll();

        log.info("Initializing Sprint 2 test data - Doctor's notes for 4 test cases");

        // Patient 1 - TestNone (1 note)
        noteRepository.save(new Note(null, "1", "Dr. Smith",
                LocalDateTime.now().minusDays(10),
                "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé"));

        // Patient 2 - TestBorderline (2 notes)
        noteRepository.save(new Note(null, "2", "Dr. Johnson",
                LocalDateTime.now().minusDays(20),
                "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement"));

        noteRepository.save(new Note(null, "2", "Dr. Johnson",
                LocalDateTime.now().minusDays(15),
                "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale"));

        // Patient 3 - TestInDanger (2 notes)
        noteRepository.save(new Note(null, "3", "Dr. Williams",
                LocalDateTime.now().minusDays(30),
                "Le patient déclare qu'il fume depuis peu"));

        noteRepository.save(new Note(null, "3", "Dr. Williams",
                LocalDateTime.now().minusDays(25),
                "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d'apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé"));

        // Patient 4 - TestEarlyOnset (4 notes)
        noteRepository.save(new Note(null, "4", "Dr. Davis",
                LocalDateTime.now().minusDays(40),
                "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d'être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments"));

        noteRepository.save(new Note(null, "4", "Dr. Davis",
                LocalDateTime.now().minusDays(35),
                "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps"));

        noteRepository.save(new Note(null, "4", "Dr. Davis",
                LocalDateTime.now().minusDays(30),
                "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé"));

        noteRepository.save(new Note(null, "4", "Dr. Davis",
                LocalDateTime.now().minusDays(25),
                "Taille, Poids, Cholestérol, Vertige et Réaction"));

        log.info("Successfully initialized {} test notes", noteRepository.count());
    }
}
