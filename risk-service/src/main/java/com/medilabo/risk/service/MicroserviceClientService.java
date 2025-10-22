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
 * Service pour communiquer avec les autres microservices
 */
@Service
@Slf4j
public class MicroserviceClientService {

    private final WebClient webClient;

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @Value("${note.service.url}")
    private String noteServiceUrl;

    public MicroserviceClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Récupère les informations d'un patient
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
     * Récupère toutes les notes d'un patient
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
            // Retourner une liste vide si aucune note n'est trouvée
            return List.of();
        }
    }
}

