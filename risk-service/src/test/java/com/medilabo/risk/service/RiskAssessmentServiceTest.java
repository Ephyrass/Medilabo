package com.medilabo.risk.service;

import com.medilabo.risk.dto.NoteDTO;
import com.medilabo.risk.dto.PatientDTO;
import com.medilabo.risk.dto.RiskAssessmentDTO;
import com.medilabo.risk.model.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RiskAssessmentService
 */
@ExtendWith(MockitoExtension.class)
class RiskAssessmentServiceTest {

    @Mock
    private MicroserviceClientService microserviceClient;

    @InjectMocks
    private RiskAssessmentService riskAssessmentService;

    private PatientDTO youngMalePatient;
    private PatientDTO youngFemalePatient;
    private PatientDTO olderMalePatient;
    private PatientDTO olderFemalePatient;

    @BeforeEach
    void setUp() {
        // Male patient < 30 years
        youngMalePatient = new PatientDTO();
        youngMalePatient.setId("1");
        youngMalePatient.setFirstName("Lucas");
        youngMalePatient.setLastName("Ferguson");
        youngMalePatient.setBirthDate(LocalDate.now().minusYears(25));
        youngMalePatient.setGender("M");

        // Female patient < 30 years
        youngFemalePatient = new PatientDTO();
        youngFemalePatient.setId("2");
        youngFemalePatient.setFirstName("Pippa");
        youngFemalePatient.setLastName("Rees");
        youngFemalePatient.setBirthDate(LocalDate.now().minusYears(27));
        youngFemalePatient.setGender("F");

        // Male patient >= 30 years
        olderMalePatient = new PatientDTO();
        olderMalePatient.setId("3");
        olderMalePatient.setFirstName("Edward");
        olderMalePatient.setLastName("Arnold");
        olderMalePatient.setBirthDate(LocalDate.now().minusYears(45));
        olderMalePatient.setGender("M");

        // Female patient >= 30 years
        olderFemalePatient = new PatientDTO();
        olderFemalePatient.setId("4");
        olderFemalePatient.setFirstName("Natalie");
        olderFemalePatient.setLastName("Clark");
        olderFemalePatient.setBirthDate(LocalDate.now().minusYears(52));
        olderFemalePatient.setGender("F");
    }

    @Test
    void testNoneRisk_NoTriggers() {
        // Arrange
        when(microserviceClient.getPatient("1")).thenReturn(youngMalePatient);
        when(microserviceClient.getPatientNotes("1")).thenReturn(List.of());

        // Act
        RiskAssessmentDTO result = riskAssessmentService.assessDiabetesRisk("1");

        // Assert
        assertNotNull(result);
        assertEquals(RiskLevel.NONE, result.getRiskLevel());
        assertEquals(0, result.getTriggerCount());
        verify(microserviceClient).getPatient("1");
        verify(microserviceClient).getPatientNotes("1");
    }

    @Test
    void testBorderlineRisk_OlderPatient_2Triggers() {
        // Arrange
        List<NoteDTO> notes = Arrays.asList(
            createNote("3", "Patient reports: Hemoglobin A1C levels slightly elevated"),
            createNote("3", "Patient has high Cholesterol")
        );
        when(microserviceClient.getPatient("3")).thenReturn(olderMalePatient);
        when(microserviceClient.getPatientNotes("3")).thenReturn(notes);

        // Act
        RiskAssessmentDTO result = riskAssessmentService.assessDiabetesRisk("3");

        // Assert
        assertNotNull(result);
        assertEquals(RiskLevel.BORDERLINE, result.getRiskLevel());
        assertEquals(2, result.getTriggerCount());
    }

    @Test
    void testInDangerRisk_YoungMale_3Triggers() {
        // Arrange
        List<NoteDTO> notes = Arrays.asList(
            createNote("1", "Patient is Smoker"),
            createNote("1", "Height: 180cm"),
            createNote("1", "Cholesterol levels are concerning")
        );
        when(microserviceClient.getPatient("1")).thenReturn(youngMalePatient);
        when(microserviceClient.getPatientNotes("1")).thenReturn(notes);

        // Act
        RiskAssessmentDTO result = riskAssessmentService.assessDiabetesRisk("1");

        // Assert
        assertNotNull(result);
        assertEquals(RiskLevel.IN_DANGER, result.getRiskLevel());
        assertEquals(3, result.getTriggerCount());
    }

    @Test
    void testInDangerRisk_YoungFemale_4Triggers() {
        // Arrange
        List<NoteDTO> notes = Arrays.asList(
            createNote("2", "Patient has Hemoglobin A1C issue"),
            createNote("2", "Smoker, needs to quit"),
            createNote("2", "Abnormal test results"),
            createNote("2", "Cholesterol level high")
        );
        when(microserviceClient.getPatient("2")).thenReturn(youngFemalePatient);
        when(microserviceClient.getPatientNotes("2")).thenReturn(notes);

        // Act
        RiskAssessmentDTO result = riskAssessmentService.assessDiabetesRisk("2");

        // Assert
        assertNotNull(result);
        assertEquals(RiskLevel.IN_DANGER, result.getRiskLevel());
        assertEquals(4, result.getTriggerCount());
    }

    @Test
    void testEarlyOnsetRisk_YoungMale_5Triggers() {
        // Arrange
        List<NoteDTO> notes = Arrays.asList(
            createNote("1", "Hemoglobin A1C elevated, Microalbumin detected"),
            createNote("1", "Patient is Smoker, Height: 180cm, Weight: 95kg"),
            createNote("1", "Cholesterol levels are Abnormal")
        );
        when(microserviceClient.getPatient("1")).thenReturn(youngMalePatient);
        when(microserviceClient.getPatientNotes("1")).thenReturn(notes);

        // Act
        RiskAssessmentDTO result = riskAssessmentService.assessDiabetesRisk("1");

        // Assert
        assertNotNull(result);
        assertEquals(RiskLevel.EARLY_ONSET, result.getRiskLevel());
        assertTrue(result.getTriggerCount() >= 5);
    }

    @Test
    void testEarlyOnsetRisk_OlderPatient_8Triggers() {
        // Arrange
        List<NoteDTO> notes = Arrays.asList(
            createNote("4", "Hemoglobin A1C, Microalbumin, Height, Weight issues"),
            createNote("4", "Smoker, Abnormal results"),
            createNote("4", "Cholesterol, Dizziness reported"),
            createNote("4", "Relapse noted, Reaction observed")
        );
        when(microserviceClient.getPatient("4")).thenReturn(olderFemalePatient);
        when(microserviceClient.getPatientNotes("4")).thenReturn(notes);

        // Act
        RiskAssessmentDTO result = riskAssessmentService.assessDiabetesRisk("4");

        // Assert
        assertNotNull(result);
        assertEquals(RiskLevel.EARLY_ONSET, result.getRiskLevel());
        assertTrue(result.getTriggerCount() >= 8);
    }

    @Test
    void testPatientNotFound_ThrowsException() {
        // Arrange
        when(microserviceClient.getPatient("999")).thenThrow(new RuntimeException("Patient not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            riskAssessmentService.assessDiabetesRisk("999");
        });
    }

    /**
     * Helper method to create a note for testing
     *
     * @param patientId the patient ID
     * @param content the note content
     * @return the created note DTO
     */
    private NoteDTO createNote(String patientId, String content) {
        NoteDTO note = new NoteDTO();
        note.setId("note-" + System.nanoTime());
        note.setPatientId(patientId);
        note.setContent(content);
        note.setCreatedAt(LocalDateTime.now());
        note.setAuthorName("Dr. Smith");
        return note;
    }
}

