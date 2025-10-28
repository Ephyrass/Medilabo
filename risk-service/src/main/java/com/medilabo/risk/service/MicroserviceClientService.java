package com.medilabo.risk.service;

import com.medilabo.risk.dto.NoteDTO;
import com.medilabo.risk.dto.PatientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service to communicate with other microservices
 */
@Service
@Slf4j
public class MicroserviceClientService {

    private final WebClient webClient;

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @Value("${note.service.url}")
    private String noteServiceUrl;

    /**
     * Constructor that initializes the WebClient
     *
     * @param webClientBuilder the WebClient builder
     */
    public MicroserviceClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Retrieve patient information
     *
     * @param patientId the patient ID
     * @return the patient data
     * @throws RuntimeException if patient cannot be fetched
     */
    public PatientDTO getPatient(String patientId) {
        log.info("Fetching patient with ID: {}", patientId);
        try {
            return webClient.get()
                    .uri(patientServiceUrl + "/api/patients/" + patientId)
                    .retrieve()
                    .bodyToMono(PatientDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching patient {}: {}", patientId, e.getMessage());
            throw new RuntimeException("Unable to fetch patient data for ID: " + patientId, e);
        }
    }

    /**
     * Retrieve all notes for a patient
     *
     * @param patientId the patient ID
     * @return list of patient notes, empty list if none found
     */
    public List<NoteDTO> getPatientNotes(String patientId) {
        log.info("Fetching notes for patient ID: {}", patientId);
        try {
            return webClient.get()
                    .uri(noteServiceUrl + "/api/notes/patient/" + patientId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<NoteDTO>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Error fetching notes for patient {}: {}", patientId, e.getMessage());
            // Return empty list if no notes found
            return List.of();
        }
    }
}

