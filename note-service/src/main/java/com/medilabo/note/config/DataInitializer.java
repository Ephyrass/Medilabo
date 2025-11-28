package com.medilabo.note.config;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;

    @Override
    public void run(String... args) throws Exception {
        if (noteRepository.count() == 0) {
            log.info("Initializing database with sample notes...");

            noteRepository.save(new Note(
                    null,
                    "1",
                    "Patient presents with elevated hemoglobin a1c levels. Weight has increased abnormally. Recommend dietary changes and regular monitoring.",
                    LocalDateTime.now().minusDays(5),
                    "Dr. Martin"
            ));

            noteRepository.save(new Note(
                    null,
                    "2",
                    "Microalbumin levels slightly elevated. Smoker status unchanged. Patient reports occasional dizziness. Continue current treatment plan.",
                    LocalDateTime.now().minusDays(3),
                    "Dr. Sarah"
            ));

            noteRepository.save(new Note(
                    null,
                    "3",
                    "Height and weight measurements normal. Cholesterol levels acceptable. No abnormal reactions observed. Patient in good health.",
                    LocalDateTime.now().minusDays(2),
                    "Dr. Johnson"
            ));

            noteRepository.save(new Note(
                    null,
                    "4",
                    "Relapse of previous condition noted. Antibodies test shows reaction. Height increased, weight stable. Need further investigation required.",
                    LocalDateTime.now().minusDays(1),
                    "Dr. Emma"
            ));

            log.info("Sample notes initialized successfully");
        } else {
            log.info("Database already contains notes, skipping initialization");
        }
    }
}

