# Risk Assessment Service - Sprint 3

## 📋 Description

Microservice d'évaluation du risque de diabète pour les patients. Ce service analyse les informations du patient et ses notes médicales pour déterminer le niveau de risque de développer du diabète.

## 🎯 Fonctionnalités

- **Évaluation du risque** : Calcule le niveau de risque de diabète pour un patient
- **Communication inter-services** : Interroge le patient-service et le note-service
- **Algorithme intelligent** : Analyse l'âge, le genre et les déclencheurs médicaux
- **API RESTful** : Exposition d'endpoints pour l'intégration frontend

## 🔍 Algorithme d'évaluation

### Déclencheurs de diabète
Le système recherche les termes suivants dans les notes médicales :
- Hémoglobine A1C
- Microalbumine
- Taille
- Poids
- Fumeur/Fumeuse
- Anormal
- Cholestérol
- Vertiges
- Rechute
- Réaction
- Anticorps

### Niveaux de risque

#### Pour les patients < 30 ans :
- **Homme** :
  - 5+ déclencheurs → Early onset
  - 3-4 déclencheurs → In Danger
  - < 3 déclencheurs → None
- **Femme** :
  - 7+ déclencheurs → Early onset
  - 4-6 déclencheurs → In Danger
  - < 4 déclencheurs → None

#### Pour les patients ≥ 30 ans :
- 8+ déclencheurs → Early onset
- 6-7 déclencheurs → In Danger
- 2-5 déclencheurs → Borderline
- < 2 déclencheurs → None

## 🚀 Endpoints

### GET /api/risk/{patientId}
Évalue le risque de diabète pour un patient donné

**Réponse** :
```json
{
  "patientId": "123",
  "patientName": "John Doe",
  "age": 45,
  "gender": "M",
  "riskLevel": "BORDERLINE",
  "triggerCount": 3,
  "message": "Patient: John Doe (age 45) diabetes assessment is Borderline"
}
```

### GET /api/risk/health
Vérifie que le service est actif

## 🛠️ Technologies

- **Spring Boot 3.5.6**
- **Java 17**
- **Spring WebFlux** (WebClient pour communication inter-services)
- **Lombok**
- **Spring Boot Actuator**

## ⚙️ Configuration

```properties
server.port=8083
patient.service.url=http://localhost:8081
note.service.url=http://localhost:8082
```

## 🏃 Démarrage

### Avec Maven
```bash
mvn spring-boot:run
```

### Avec Docker
```bash
docker build -t risk-service .
docker run -p 8083:8083 risk-service
```

## 📦 Build

```bash
mvn clean package
```

## 🔗 Dépendances

- Patient Service (port 8081)
- Note Service (port 8082)

## 📊 Architecture

```
Risk Service (8083)
    ↓
    ├─→ Patient Service (8081) : Récupère les infos du patient
    └─→ Note Service (8082)    : Récupère les notes médicales
```

## 🧪 Tests

Le service peut être testé avec curl :

```bash
# Test d'évaluation du risque
curl http://localhost:8083/api/risk/123

# Test de santé
curl http://localhost:8083/api/risk/health
```

## 📝 Notes

- Ce microservice n'a pas de base de données dédiée
- Il effectue des appels synchrones aux autres services
- Les résultats sont calculés en temps réel à chaque requête
package com.medilabo.risk.controller;

import com.medilabo.risk.dto.RiskAssessmentDTO;
import com.medilabo.risk.service.RiskAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'évaluation du risque de diabète
 */
@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    /**
     * Évalue le risque de diabète pour un patient donné
     * GET /api/risk/{patientId}
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<RiskAssessmentDTO> assessRisk(@PathVariable String patientId) {
        log.info("Received risk assessment request for patient ID: {}", patientId);
        
        try {
            RiskAssessmentDTO assessment = riskAssessmentService.assessDiabetesRisk(patientId);
            return ResponseEntity.ok(assessment);
        } catch (RuntimeException e) {
            log.error("Error assessing risk for patient {}: {}", patientId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint de santé pour vérifier que le service est actif
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Risk Assessment Service is running");
    }
}

