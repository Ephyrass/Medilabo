package com.medilabo.patient.service;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PatientService
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = new Patient();
        testPatient.setId("1");
        testPatient.setFirstName("John");
        testPatient.setLastName("Doe");
        testPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        testPatient.setGender("M");
        testPatient.setAddress("123 Main St");
        testPatient.setPhoneNumber("555-1234");
    }

    @Test
    void testGetAllPatients_ShouldReturnAllPatients() {
        // Arrange
        List<Patient> patients = Arrays.asList(testPatient, new Patient());
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> result = patientService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById_WhenPatientExists_ShouldReturnPatient() {
        // Arrange
        when(patientRepository.findById("1")).thenReturn(Optional.of(testPatient));

        // Act
        Optional<Patient> result = patientService.getPatientById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        verify(patientRepository, times(1)).findById("1");
    }

    @Test
    void testGetPatientById_WhenPatientDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(patientRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<Patient> result = patientService.getPatientById("999");

        // Assert
        assertFalse(result.isPresent());
        verify(patientRepository, times(1)).findById("999");
    }

    @Test
    void testAddPatient_ShouldSaveAndReturnPatient() {
        // Arrange
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // Act
        Patient result = patientService.addPatient(testPatient);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(patientRepository, times(1)).save(testPatient);
    }

    @Test
    void testUpdatePatient_WhenPatientExists_ShouldUpdateAndReturn() {
        // Arrange
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Jane");
        updatedPatient.setLastName("Smith");
        updatedPatient.setBirthDate(LocalDate.of(1985, 5, 15));
        updatedPatient.setGender("F");
        updatedPatient.setAddress("456 Oak Ave");
        updatedPatient.setPhoneNumber("555-5678");

        when(patientRepository.findById("1")).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // Act
        Optional<Patient> result = patientService.updatePatient("1", updatedPatient);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
        verify(patientRepository, times(1)).findById("1");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testUpdatePatient_WhenPatientDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        Patient updatedPatient = new Patient();
        when(patientRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<Patient> result = patientService.updatePatient("999", updatedPatient);

        // Assert
        assertFalse(result.isPresent());
        verify(patientRepository, times(1)).findById("999");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testDeletePatient_WhenPatientExists_ShouldReturnTrue() {
        // Arrange
        when(patientRepository.existsById("1")).thenReturn(true);
        doNothing().when(patientRepository).deleteById("1");

        // Act
        boolean result = patientService.deletePatient("1");

        // Assert
        assertTrue(result);
        verify(patientRepository, times(1)).existsById("1");
        verify(patientRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeletePatient_WhenPatientDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(patientRepository.existsById("999")).thenReturn(false);

        // Act
        boolean result = patientService.deletePatient("999");

        // Assert
        assertFalse(result);
        verify(patientRepository, times(1)).existsById("999");
        verify(patientRepository, never()).deleteById(anyString());
    }
}

