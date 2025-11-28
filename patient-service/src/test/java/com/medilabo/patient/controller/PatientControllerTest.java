package com.medilabo.patient.controller;

import com.medilabo.patient.model.ContactInfo;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PatientController
 */
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    private Patient testPatient;
    private ContactInfo testContactInfo;

    @BeforeEach
    void setUp() {
        testContactInfo = new ContactInfo();
        testContactInfo.setId(1L);
        testContactInfo.setAddress("123 Main St");
        testContactInfo.setPhoneNumber("555-1234");

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setFirstName("John");
        testPatient.setLastName("Doe");
        testPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        testPatient.setGender("M");
        testPatient.setContactInfo(testContactInfo);
    }

    @Test
    void testGetAllPatients_ShouldReturnPatientsList() throws Exception {
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(testPatient));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));

        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    void testGetPatientById_WhenPatientExists_ShouldReturnPatient() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(testPatient));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.gender").value("M"));

        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    void testGetPatientById_WhenPatientDoesNotExist_ShouldReturn404() throws Exception {
        when(patientService.getPatientById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());

        verify(patientService, times(1)).getPatientById(999L);
    }

    @Test
    void testAddPatient_WithValidData_ShouldReturnCreated() throws Exception {
        when(patientService.addPatient(any(Patient.class))).thenReturn(testPatient);

        String patientJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthDate": "1990-01-01",
                    "gender": "M",
                    "contactInfo": {
                        "address": "123 Main St",
                        "phoneNumber": "555-1234"
                    }
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(patientService, times(1)).addPatient(any(Patient.class));
    }

    @Test
    void testUpdatePatient_WhenPatientExists_ShouldReturnUpdatedPatient() throws Exception {
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(Optional.of(testPatient));

        String patientJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthDate": "1990-01-01",
                    "gender": "M",
                    "contactInfo": {
                        "address": "123 Main St",
                        "phoneNumber": "555-1234"
                    }
                }
                """;

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(patientService, times(1)).updatePatient(eq(1L), any(Patient.class));
    }

    @Test
    void testUpdatePatient_WhenPatientDoesNotExist_ShouldReturn404() throws Exception {
        when(patientService.updatePatient(eq(999L), any(Patient.class))).thenReturn(Optional.empty());

        String patientJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthDate": "1990-01-01",
                    "gender": "M"
                }
                """;

        mockMvc.perform(put("/api/patients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isNotFound());

        verify(patientService, times(1)).updatePatient(eq(999L), any(Patient.class));
    }

    @Test
    void testDeletePatient_WhenPatientExists_ShouldReturn204() throws Exception {
        when(patientService.deletePatient(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void testDeletePatient_WhenPatientDoesNotExist_ShouldReturn404() throws Exception {
        when(patientService.deletePatient(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/patients/999"))
                .andExpect(status().isNotFound());

        verify(patientService, times(1)).deletePatient(999L);
    }
}

