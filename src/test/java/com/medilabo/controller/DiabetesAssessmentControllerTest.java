package com.medilabo.controller;

import com.medilabo.model.DiabetesRiskLevel;
import com.medilabo.model.Note;
import com.medilabo.model.Patient;
import com.medilabo.repository.NoteRepository;
import com.medilabo.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for DiabetesAssessmentController
 * Sprint 3 - Test diabetes risk assessment endpoints
 */
@SpringBootTest
@AutoConfigureMockMvc
public class DiabetesAssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientService patientService;

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        // Clear existing data
        noteRepository.deleteAll();
    }

    /**
     * Test User Story: Générer un rapport de diabète
     * Scenario: Patient with NONE risk level
     */
    @Test
    void assessDiabetesRisk_patientTestNone_shouldReturnNoneRisk() throws Exception {
        mockMvc.perform(get("/api/assess/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value("1"))
                .andExpect(jsonPath("$.riskLevel").value("NONE"))
                .andExpect(jsonPath("$.age").exists())
                .andExpect(jsonPath("$.gender").exists())
                .andExpect(jsonPath("$.triggerCount").exists());
    }

    /**
     * Test User Story: Patient with BORDERLINE risk level
     */
    @Test
    void assessDiabetesRisk_patientTestBorderline_shouldReturnBorderlineRisk() throws Exception {
        // Add notes with 2-5 triggers for patient 2 (age > 30)
        noteRepository.save(new Note(null, "2", "TestBorderline",
                LocalDateTime.now(), "Patient reports stress, Hémoglobine A1C elevated, Microalbumine present"));

        mockMvc.perform(get("/api/assess/id/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value("2"))
                .andExpect(jsonPath("$.riskLevel").value("BORDERLINE"))
                .andExpect(jsonPath("$.triggerCount").value(2));
    }

    /**
     * Test User Story: Young male patient with IN_DANGER risk level
     */
    @Test
    void assessDiabetesRisk_youngMaleWithTriggers_shouldReturnInDanger() throws Exception {
        // Patient 3 is young male
        // Add notes with 3 triggers
        noteRepository.save(new Note(null, "3", "TestInDanger",
                LocalDateTime.now(), "Patient is Fumeur, has Cholestérol issues, abnormal Poids"));

        mockMvc.perform(get("/api/assess/id/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value("3"))
                .andExpect(jsonPath("$.riskLevel").value("IN_DANGER"))
                .andExpect(jsonPath("$.triggerCount").value(3));
    }

    /**
     * Test User Story: Young female patient with EARLY_ONSET risk level
     */
    @Test
    void assessDiabetesRisk_youngFemaleWithManyTriggers_shouldReturnEarlyOnset() throws Exception {
        // Patient 4 is young female
        // Add notes with 7+ triggers
        noteRepository.save(new Note(null, "4", "TestEarlyOnset",
                LocalDateTime.now(),
                "Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeuse, Cholestérol, Anticorps"));

        mockMvc.perform(get("/api/assess/id/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value("4"))
                .andExpect(jsonPath("$.riskLevel").value("EARLY_ONSET"))
                .andExpect(jsonPath("$.triggerCount").value(7));
    }

    /**
     * Test: Patient not found should return 404
     */
    @Test
    void assessDiabetesRisk_patientNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/assess/id/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test: Report contains all required fields
     */
    @Test
    void assessDiabetesRisk_shouldContainAllReportFields() throws Exception {
        mockMvc.perform(get("/api/assess/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").exists())
                .andExpect(jsonPath("$.patientName").exists())
                .andExpect(jsonPath("$.age").exists())
                .andExpect(jsonPath("$.gender").exists())
                .andExpect(jsonPath("$.riskLevel").exists())
                .andExpect(jsonPath("$.triggerCount").exists());
    }
}

