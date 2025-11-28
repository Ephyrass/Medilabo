package com.medilabo.patient.service;

import com.medilabo.patient.model.ContactInfo;
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
    void testGetAllPatients_ShouldReturnAllPatients() {
        List<Patient> patients = Arrays.asList(testPatient, new Patient());
        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById_WhenPatientExists_ShouldReturnPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        Optional<Patient> result = patientService.getPatientById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPatientById_WhenPatientDoesNotExist_ShouldReturnEmpty() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Patient> result = patientService.getPatientById(999L);

        assertFalse(result.isPresent());
        verify(patientRepository, times(1)).findById(999L);
    }

    @Test
    void testAddPatient_ShouldSaveAndReturnPatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        Patient result = patientService.addPatient(testPatient);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(patientRepository, times(1)).save(testPatient);
    }

    @Test
    void testUpdatePatient_WhenPatientExists_ShouldUpdateAndReturn() {
        ContactInfo updatedContactInfo = new ContactInfo();
        updatedContactInfo.setId(2L);
        updatedContactInfo.setAddress("456 Oak Ave");
        updatedContactInfo.setPhoneNumber("555-5678");

        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Jane");
        updatedPatient.setLastName("Smith");
        updatedPatient.setBirthDate(LocalDate.of(1985, 5, 15));
        updatedPatient.setGender("F");
        updatedPatient.setContactInfo(updatedContactInfo);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        Optional<Patient> result = patientService.updatePatient(1L, updatedPatient);

        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testUpdatePatient_WhenPatientDoesNotExist_ShouldReturnEmpty() {
        Patient updatedPatient = new Patient();
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Patient> result = patientService.updatePatient(999L, updatedPatient);

        assertFalse(result.isPresent());
        verify(patientRepository, times(1)).findById(999L);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testDeletePatient_WhenPatientExists_ShouldReturnTrue() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patientRepository).deleteById(1L);

        boolean result = patientService.deletePatient(1L);

        assertTrue(result);
        verify(patientRepository, times(1)).existsById(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatient_WhenPatientDoesNotExist_ShouldReturnFalse() {
        when(patientRepository.existsById(999L)).thenReturn(false);

        boolean result = patientService.deletePatient(999L);

        assertFalse(result);
        verify(patientRepository, times(1)).existsById(999L);
        verify(patientRepository, never()).deleteById(any());
    }
}

