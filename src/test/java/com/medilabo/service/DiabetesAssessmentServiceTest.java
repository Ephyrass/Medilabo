package com.medilabo.service;

import com.medilabo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for DiabetesAssessmentService
 * Sprint 3 - Test all business rules for diabetes risk assessment
 */
@ExtendWith(MockitoExtension.class)
public class DiabetesAssessmentServiceTest {

    @Mock
    private IPatientService patientService;

    @Mock
    private INoteService noteService;

    @InjectMocks
    private DiabetesAssessmentService assessmentService;

    private Patient youngMalePatient;
    private Patient youngFemalePatient;
    private Patient olderMalePatient;
    private Patient olderFemalePatient;

    @BeforeEach
    void setUp() {
        // Young male patient (25 years old)
        youngMalePatient = new Patient("1", "John", "Young",
                LocalDate.now().minusYears(25), "M", "123 Main St", "555-1234");

        // Young female patient (28 years old)
        youngFemalePatient = new Patient("2", "Jane", "Young",
                LocalDate.now().minusYears(28), "F", "456 Oak St", "555-5678");

        // Older male patient (45 years old)
        olderMalePatient = new Patient("3", "Bob", "Senior",
                LocalDate.now().minusYears(45), "M", "789 Pine St", "555-9012");

        // Older female patient (50 years old)
        olderFemalePatient = new Patient("4", "Alice", "Elder",
                LocalDate.now().minusYears(50), "F", "321 Elm St", "555-3456");
    }

    /**
     * Test: NONE - No trigger terms
     */
    @Test
    void generateReport_noTriggers_shouldReturnNone() {
        when(patientService.getPatientById("1")).thenReturn(Optional.of(youngMalePatient));
        when(noteService.getNotesByPatientId("1"))
                .thenReturn(Collections.singletonList(
                        new Note("n1", "1", "John Young", LocalDateTime.now(),
                                "Patient is feeling great today")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("1");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.NONE);
        assertThat(report.get().getTriggerCount()).isEqualTo(0);
    }

    /**
     * Test: BORDERLINE - 2-5 triggers, age > 30
     */
    @Test
    void generateReport_borderline_shouldReturnBorderline() {
        when(patientService.getPatientById("3")).thenReturn(Optional.of(olderMalePatient));
        when(noteService.getNotesByPatientId("3"))
                .thenReturn(Arrays.asList(
                        new Note("n1", "3", "Bob Senior", LocalDateTime.now(),
                                "Patient reports Poids increase and Taille measurement needed"),
                        new Note("n2", "3", "Bob Senior", LocalDateTime.now(),
                                "Cholestérol levels checked")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("3");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.BORDERLINE);
        assertThat(report.get().getTriggerCount()).isEqualTo(3);
        assertThat(report.get().getAge()).isGreaterThan(30);
    }

    /**
     * Test: IN_DANGER - Male < 30 with 3 triggers
     */
    @Test
    void generateReport_youngMale3Triggers_shouldReturnInDanger() {
        when(patientService.getPatientById("1")).thenReturn(Optional.of(youngMalePatient));
        when(noteService.getNotesByPatientId("1"))
                .thenReturn(Collections.singletonList(
                        new Note("n1", "1", "John Young", LocalDateTime.now(),
                                "Patient is Fumeur, has high Cholestérol, and abnormal Poids")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("1");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.IN_DANGER);
        assertThat(report.get().getTriggerCount()).isEqualTo(3);
    }

    /**
     * Test: IN_DANGER - Female < 30 with 4 triggers
     */
    @Test
    void generateReport_youngFemale4Triggers_shouldReturnInDanger() {
        when(patientService.getPatientById("2")).thenReturn(Optional.of(youngFemalePatient));
        when(noteService.getNotesByPatientId("2"))
                .thenReturn(Collections.singletonList(
                        new Note("n1", "2", "Jane Young", LocalDateTime.now(),
                                "Patient shows Réaction, has Vertige, high Cholestérol, and abnormal Poids")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("2");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.IN_DANGER);
        assertThat(report.get().getTriggerCount()).isEqualTo(4);
    }

    /**
     * Test: IN_DANGER - Age > 30 with 6-7 triggers
     */
    @Test
    void generateReport_older6Triggers_shouldReturnInDanger() {
        when(patientService.getPatientById("3")).thenReturn(Optional.of(olderMalePatient));
        when(noteService.getNotesByPatientId("3"))
                .thenReturn(Collections.singletonList(
                        new Note("n1", "3", "Bob Senior", LocalDateTime.now(),
                                "Patient has Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeur, Cholestérol issues")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("3");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.IN_DANGER);
        assertThat(report.get().getTriggerCount()).isEqualTo(6);
    }

    /**
     * Test: EARLY_ONSET - Male < 30 with 5+ triggers
     */
    @Test
    void generateReport_youngMale5Triggers_shouldReturnEarlyOnset() {
        when(patientService.getPatientById("1")).thenReturn(Optional.of(youngMalePatient));
        when(noteService.getNotesByPatientId("1"))
                .thenReturn(Collections.singletonList(
                        new Note("n1", "1", "John Young", LocalDateTime.now(),
                                "Hémoglobine A1C elevated, Microalbumine present, Fumeur, Cholestérol high, Réaction observed")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("1");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.EARLY_ONSET);
        assertThat(report.get().getTriggerCount()).isEqualTo(5);
    }

    /**
     * Test: EARLY_ONSET - Female < 30 with 7+ triggers
     */
    @Test
    void generateReport_youngFemale7Triggers_shouldReturnEarlyOnset() {
        when(patientService.getPatientById("2")).thenReturn(Optional.of(youngFemalePatient));
        when(noteService.getNotesByPatientId("2"))
                .thenReturn(Collections.singletonList(
                        new Note("n1", "2", "Jane Young", LocalDateTime.now(),
                                "Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeuse, Cholestérol, Anticorps detected")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("2");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.EARLY_ONSET);
        assertThat(report.get().getTriggerCount()).isEqualTo(7);
    }

    /**
     * Test: EARLY_ONSET - Age > 30 with 8+ triggers
     */
    @Test
    void generateReport_older8Triggers_shouldReturnEarlyOnset() {
        when(patientService.getPatientById("4")).thenReturn(Optional.of(olderFemalePatient));
        when(noteService.getNotesByPatientId("4"))
                .thenReturn(Arrays.asList(
                        new Note("n1", "4", "Alice Elder", LocalDateTime.now(),
                                "Hémoglobine A1C elevated, Microalbumine, Taille, Poids abnormal"),
                        new Note("n2", "4", "Alice Elder", LocalDateTime.now(),
                                "Fumeur status, Cholestérol high, Vertige, Réaction noted")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("4");

        assertThat(report).isPresent();
        assertThat(report.get().getRiskLevel()).isEqualTo(DiabetesRiskLevel.EARLY_ONSET);
        assertThat(report.get().getTriggerCount()).isEqualTo(8);
    }

    /**
     * Test: Patient not found
     */
    @Test
    void generateReport_patientNotFound_shouldReturnEmpty() {
        when(patientService.getPatientById("999")).thenReturn(Optional.empty());

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("999");

        assertThat(report).isEmpty();
    }

    /**
     * Test: Trigger terms are case-insensitive
     */
    @Test
    void generateReport_caseInsensitiveTriggers_shouldCountCorrectly() {
        when(patientService.getPatientById("1")).thenReturn(Optional.of(youngMalePatient));
        when(noteService.getNotesByPatientId("1"))
                .thenReturn(Collections.singletonList(
                        new Note("n1", "1", "John Young", LocalDateTime.now(),
                                "HÉMOGLOBINE A1C, microalbumine, TAILLE checked")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("1");

        assertThat(report).isPresent();
        assertThat(report.get().getTriggerCount()).isEqualTo(3);
    }

    /**
     * Test: Multiple notes are combined for trigger search
     */
    @Test
    void generateReport_multipleNotes_shouldCombineForTriggers() {
        when(patientService.getPatientById("1")).thenReturn(Optional.of(youngMalePatient));
        when(noteService.getNotesByPatientId("1"))
                .thenReturn(Arrays.asList(
                        new Note("n1", "1", "John Young", LocalDateTime.now(), "Hémoglobine A1C elevated"),
                        new Note("n2", "1", "John Young", LocalDateTime.now(), "Cholestérol high"),
                        new Note("n3", "1", "John Young", LocalDateTime.now(), "Patient is Fumeur")));

        Optional<DiabetesReport> report = assessmentService.generateDiabetesReport("1");

        assertThat(report).isPresent();
        assertThat(report.get().getTriggerCount()).isEqualTo(3);
    }
}

