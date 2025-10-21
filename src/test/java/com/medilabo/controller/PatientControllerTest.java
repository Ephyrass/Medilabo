package com.medilabo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test User Story: Vue des informations personnelles des patients
     */
    @Test
    void getPatients_shouldReturnAllPatientsWithAllFields() throws Exception {
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[0].lastName").exists())
                .andExpect(jsonPath("$[0].birthDate").exists())
                .andExpect(jsonPath("$[0].gender").exists())
                .andExpect(jsonPath("$[0].address").exists())
                .andExpect(jsonPath("$[0].phoneNumber").exists());
    }

    /**
     * Test User Story: Vue des informations personnelles d'un patient spécifique
     */
    @Test
    void getPatientById_shouldReturnPatientDetails() throws Exception {
        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstName").value("TestNone"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andExpect(jsonPath("$.birthDate").value("1966-12-31"))
                .andExpect(jsonPath("$.gender").value("F"))
                .andExpect(jsonPath("$.address").value("1 Brookside St"))
                .andExpect(jsonPath("$.phoneNumber").value("100-222-3333"));
    }

    @Test
    void getPatientById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test User Story: Ajouter des informations personnelles des patients
     */
    @Test
    void addPatient_shouldCreateNewPatient() throws Exception {
        String newPatientJson = """
                {
                    "id": "5",
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthDate": "1990-05-15",
                    "gender": "M",
                    "address": "123 Main St",
                    "phoneNumber": "555-1234"
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPatientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    /**
     * Test User Story: Ajouter un patient avec adresse et téléphone optionnels
     */
    @Test
    void addPatient_shouldCreatePatientWithoutOptionalFields() throws Exception {
        String newPatientJson = """
                {
                    "id": "6",
                    "firstName": "Jane",
                    "lastName": "Smith",
                    "birthDate": "1985-08-20",
                    "gender": "F"
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPatientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    /**
     * Test User Story: Mise à jour des informations personnelles
     */
    @Test
    void updatePatient_shouldUpdateExistingPatient() throws Exception {
        String updatedPatientJson = """
                {
                    "firstName": "TestNone",
                    "lastName": "Updated",
                    "birthDate": "1966-12-31",
                    "gender": "F",
                    "address": "999 New Address St",
                    "phoneNumber": "999-888-7777"
                }
                """;

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPatientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.lastName").value("Updated"))
                .andExpect(jsonPath("$.address").value("999 New Address St"))
                .andExpect(jsonPath("$.phoneNumber").value("999-888-7777"));
    }

    @Test
    void updatePatient_shouldReturn404WhenNotFound() throws Exception {
        String updatedPatientJson = """
                {
                    "firstName": "Test",
                    "lastName": "NotFound",
                    "birthDate": "1990-01-01",
                    "gender": "M"
                }
                """;

        mockMvc.perform(put("/api/patients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPatientJson))
                .andExpect(status().isNotFound());
    }
}

