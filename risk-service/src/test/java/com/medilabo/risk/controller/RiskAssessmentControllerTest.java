package com.medilabo.risk.controller;

import com.medilabo.risk.dto.RiskAssessmentDTO;
import com.medilabo.risk.model.RiskLevel;
import com.medilabo.risk.service.RiskAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for RiskAssessmentController
 */
@WebMvcTest(RiskAssessmentController.class)
class RiskAssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiskAssessmentService riskAssessmentService;

    private RiskAssessmentDTO testAssessment;

    @BeforeEach
    void setUp() {
        testAssessment = new RiskAssessmentDTO(
                "patient1",
                "John Doe",
                30,
                "M",
                RiskLevel.BORDERLINE,
                3,
                "Patient: John Doe (age 30) diabetes assessment is Borderline"
        );
    }

    @Test
    void testAssessRisk_WhenPatientExists_ShouldReturnRiskAssessment() throws Exception {
        // Arrange
        when(riskAssessmentService.assessDiabetesRisk("patient1")).thenReturn(testAssessment);

        // Act & Assert
        mockMvc.perform(get("/api/risk/patient1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientId").value("patient1"))
                .andExpect(jsonPath("$.patientName").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.riskLevel").value("BORDERLINE"))
                .andExpect(jsonPath("$.triggerCount").value(3));

        verify(riskAssessmentService, times(1)).assessDiabetesRisk("patient1");
    }

    @Test
    void testAssessRisk_WhenPatientNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(riskAssessmentService.assessDiabetesRisk("patient999"))
                .thenThrow(new RuntimeException("Patient not found"));

        // Act & Assert
        mockMvc.perform(get("/api/risk/patient999"))
                .andExpect(status().isNotFound());

        verify(riskAssessmentService, times(1)).assessDiabetesRisk("patient999");
    }

    @Test
    void testHealth_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/risk/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Risk Service is running"));
    }

    @Test
    void testAssessRisk_WithNoneRisk_ShouldReturnCorrectAssessment() throws Exception {
        // Arrange
        RiskAssessmentDTO noneRisk = new RiskAssessmentDTO(
                "patient2",
                "Jane Smith",
                25,
                "F",
                RiskLevel.NONE,
                0,
                "Patient: Jane Smith (age 25) diabetes assessment is None"
        );
        when(riskAssessmentService.assessDiabetesRisk("patient2")).thenReturn(noneRisk);

        // Act & Assert
        mockMvc.perform(get("/api/risk/patient2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("NONE"))
                .andExpect(jsonPath("$.triggerCount").value(0));

        verify(riskAssessmentService, times(1)).assessDiabetesRisk("patient2");
    }

    @Test
    void testAssessRisk_WithInDangerRisk_ShouldReturnCorrectAssessment() throws Exception {
        // Arrange
        RiskAssessmentDTO inDangerRisk = new RiskAssessmentDTO(
                "patient3",
                "Bob Johnson",
                28,
                "M",
                RiskLevel.IN_DANGER,
                4,
                "Patient: Bob Johnson (age 28) diabetes assessment is In Danger"
        );
        when(riskAssessmentService.assessDiabetesRisk("patient3")).thenReturn(inDangerRisk);

        // Act & Assert
        mockMvc.perform(get("/api/risk/patient3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("IN_DANGER"))
                .andExpect(jsonPath("$.triggerCount").value(4));

        verify(riskAssessmentService, times(1)).assessDiabetesRisk("patient3");
    }

    @Test
    void testAssessRisk_WithEarlyOnsetRisk_ShouldReturnCorrectAssessment() throws Exception {
        // Arrange
        RiskAssessmentDTO earlyOnsetRisk = new RiskAssessmentDTO(
                "patient4",
                "Alice Brown",
                27,
                "F",
                RiskLevel.EARLY_ONSET,
                8,
                "Patient: Alice Brown (age 27) diabetes assessment is Early onset"
        );
        when(riskAssessmentService.assessDiabetesRisk("patient4")).thenReturn(earlyOnsetRisk);

        // Act & Assert
        mockMvc.perform(get("/api/risk/patient4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("EARLY_ONSET"))
                .andExpect(jsonPath("$.triggerCount").value(8));

        verify(riskAssessmentService, times(1)).assessDiabetesRisk("patient4");
    }
}

