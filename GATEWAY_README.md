- `GET http://localhost:8080/api/patients/{id}` → Patient Service (8081)

### Notes
- `GET http://localhost:8080/api/notes` → Note Service (8082)
- `GET http://localhost:8080/api/notes/patient/{patientId}` → Note Service (8082)
- `POST http://localhost:8080/api/notes` → Note Service (8082)
- `PUT http://localhost:8080/api/notes/{id}` → Note Service (8082)
- `DELETE http://localhost:8080/api/notes/{id}` → Note Service (8082)

### Risk Assessment (Sprint 3)
- `GET http://localhost:8080/api/risk/{patientId}` → Risk Service (8083)
- `GET http://localhost:8080/api/assess/{patientId}` → Risk Service (8083)

## 🛡️ Fonctionnalités

### 1. Circuit Breaker (Resilience4j)
Le Gateway implémente un circuit breaker pour chaque service :
- **Retry automatique** : 3 tentatives en cas d'échec
- **Fallback** : Messages d'erreur conviviaux si le service est indisponible
- **Seuil d'échec** : 50% des requêtes échouées déclenchent l'ouverture du circuit
- **Temps d'attente** : 10 secondes avant de réessayer

### 2. CORS
Configuration CORS permettant les requêtes depuis :
- `http://localhost:3000` (React)
- `http://localhost:4200` (Angular)
- `http://localhost:8080` (Gateway)

### 3. Logging
Chaque requête est loggée avec :
- Méthode HTTP et chemin
- Code de statut de la réponse
- Durée d'exécution

## 🚀 Démarrage

### Prérequis
- Java 17+
- Maven
- MongoDB (pour le Note Service)

### Démarrer MongoDB
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### Démarrer l'application (Gateway + Services)
```bash
mvnw.cmd spring-boot:run
```

Le Gateway démarre sur le port **8080**.

## 🧪 Tester le Gateway

### Test avec curl
```bash
# Récupérer tous les patients
curl http://localhost:8080/api/patients

# Récupérer toutes les notes
curl http://localhost:8080/api/notes

# Récupérer les notes d'un patient
curl http://localhost:8080/api/notes/patient/1

# Créer une note
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{"patientId":"1","patientName":"TestNone","content":"Nouvelle note"}'
```

### Test du Fallback
Si un service est arrêté, le circuit breaker renvoie un fallback :
```bash
curl http://localhost:8080/api/patients
# Réponse si le service est down :
# {
#   "error": "Patient Service is temporarily unavailable",
#   "message": "Please try again later",
#   "service": "patient-service"
# }
```

## 📊 Monitoring

Les logs du Gateway affichent :
```
INFO Gateway Request: GET /api/patients
INFO Gateway Response: GET /api/patients - Status: 200 - Duration: 45ms
```

## 🔒 Sécurité

La sécurité est désactivée pour les tests (configuré dans `SecurityConfig`).
Pour la production :
1. Activer l'authentification JWT au niveau du Gateway
2. Configurer Spring Security avec OAuth2
3. Ajouter des rate limiters

## 📁 Structure du Code

```
gateway/
├── config/
│   ├── GatewayConfig.java      # Configuration des routes
│   └── CorsConfig.java          # Configuration CORS
├── controller/
│   └── FallbackController.java  # Endpoints de fallback
└── filter/
    └── LoggingFilter.java       # Filtre de logging global
```

## 🎯 Prochaines étapes (Sprint 3)

1. Implémenter le **Risk Assessment Service** (port 8083)
2. Ajouter les routes d'évaluation du risque dans le Gateway
3. Implémenter la logique d'analyse des termes déclencheurs
4. Calculer le niveau de risque (None, Borderline, In Danger, Early Onset)

## 🐛 Troubleshooting

### Le Gateway ne démarre pas
- Vérifier que le port 8080 est libre
- Vérifier les dépendances Maven avec `mvnw.cmd dependency:tree`

### Circuit breaker toujours ouvert
- Vérifier que MongoDB est démarré
- Vérifier les logs pour identifier le service défaillant
- Augmenter le `waitDurationInOpenState` dans application.properties

### CORS errors
- Vérifier que votre frontend utilise un des origins autorisés
- Ajouter votre origin dans `CorsConfig.java`
# Medilabo - Spring Cloud Gateway

## 🚀 Architecture Microservices

Ce projet implémente un API Gateway avec Spring Cloud Gateway pour router les requêtes vers les différents microservices de Medilabo.

## 📋 Services et Ports

| Service | Port | Description |
|---------|------|-------------|
| **Gateway** | 8080 | Point d'entrée unique (API Gateway) |
| **Patient Service** | 8081 | Gestion des patients (en mémoire) |
| **Note Service** | 8082 | Gestion des notes médicales (MongoDB) |
| **Risk Service** | 8083 | Évaluation du risque diabète (Sprint 3) |

## 🔀 Routes du Gateway

### Patients
- `GET http://localhost:8080/api/patients` → Patient Service (8081)

