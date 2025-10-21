# Medilabo - Application de Gestion des Dossiers Patients

## 🏥 Description

Medilabo est une application de gestion des dossiers patients basée sur une **architecture microservices**. Elle permet aux organisateurs et praticiens de gérer les informations des patients, leurs notes médicales et d'évaluer les risques de diabète.

## 🏗️ Architecture Microservices

```
┌─────────────────────────────────────────────────────────┐
│                    Client / Frontend                     │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP Requests
                         │ http://localhost:8080
                         ▼
┌─────────────────────────────────────────────────────────┐
│           Spring Cloud Gateway (Port 8080)              │
│  - Routage intelligent                                   │
│  - Circuit Breaker (Resilience4j)                       │
│  - CORS Configuration                                    │
│  - Retry Logic                                           │
└─────┬───────────────────┬───────────────────┬──────────┘
      │                   │                   │
      │ /api/patients/**  │ /api/notes/**    │ /api/assess/**
      ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│   Patient    │   │     Note     │   │     Risk     │
│   Service    │   │   Service    │   │  Assessment  │
│              │   │              │   │   Service    │
│  Port 8081   │   │  Port 8082   │   │  Port 8083   │
│              │   │              │   │              │
│   MongoDB    │   │   MongoDB    │   │  Sprint 3    │
└──────────────┘   └──────┬───────┘   └──────────────┘
                          │
                          ▼
                   ┌──────────────┐
                   │   MongoDB    │
                   │ Port 27017   │
                   └──────────────┘
```

## 📦 Microservices

### 1. Patient Service (Port 8081) - ✅ Sprint 1
Gestion des dossiers patients avec MongoDB.

**Endpoints REST :**
- `GET /api/patients` - Liste tous les patients
- `GET /api/patients/{id}` - Détails d'un patient
- `POST /api/patients` - Ajouter un patient
- `PUT /api/patients/{id}` - Mettre à jour un patient
- `DELETE /api/patients/{id}` - Supprimer un patient

**User Stories Sprint 1 :**
- ✅ Vue des infos personnelles des patients
- ✅ Mise à jour des informations personnelles
- ✅ Ajouter des informations personnelles des patients

### 2. Gateway (Port 8080) - ✅ Sprint 1
Point d'entrée unique avec Spring Cloud Gateway.

**Fonctionnalités :**
- Routage vers les microservices
- Circuit Breaker avec Resilience4j
- Retry automatique (3 tentatives)
- Configuration CORS
- Fallback en cas d'erreur

### 3. Note Service (Port 8082) - 🚧 Sprint 2
Gestion des notes médicales avec MongoDB (à venir).

### 4. Risk Assessment Service (Port 8083) - 🚧 Sprint 3
Évaluation du risque de diabète (à venir).

## 🚀 Démarrage Rapide

### Prérequis
- Java 17
- Maven
- Docker & Docker Compose
- MongoDB

### 1. Démarrer MongoDB
```bash
docker-compose up -d mongodb
```

### 2. Démarrer le Patient Service
```bash
cd patient-service
mvnw.cmd spring-boot:run
```

### 3. Démarrer le Gateway
```bash
cd gateway
mvnw.cmd spring-boot:run
```

### 4. Tester l'API
```bash
# Via le Gateway (recommandé)
curl http://localhost:8080/api/patients

# Directement au Patient Service
curl http://localhost:8081/api/patients
```

## 📊 Données de Test

L'application charge automatiquement 4 patients de test au démarrage :

1. **TestNone Test** (F, 58 ans) - Aucun risque
2. **TestBorderline Test** (M, 79 ans) - Risque limité
3. **TestInDanger Test** (M, 21 ans) - En danger
4. **TestEarlyOnset Test** (F, 23 ans) - Apparition précoce

## 🐳 Docker

Chaque microservice possède son propre Dockerfile pour la conteneurisation.

### Build des images Docker
```bash
# Patient Service
cd patient-service
mvnw.cmd clean package
docker build -t medilabo/patient-service .

# Gateway
cd gateway
mvnw.cmd clean package
docker build -t medilabo/gateway .
```

### Lancer tous les services avec Docker Compose
```bash
docker-compose up -d
```

## 🛠️ Technologies Utilisées

- **Backend :** Spring Boot 3.5.6, Java 17
- **API Gateway :** Spring Cloud Gateway
- **Base de données :** MongoDB 6.0
- **Résilience :** Resilience4j (Circuit Breaker)
- **Build :** Maven
- **Conteneurisation :** Docker

## 📁 Structure du Projet

```
Medilabo/
├── docker-compose.yml          # Orchestration Docker
├── README.md                   # Ce fichier
├── ARCHITECTURE.md             # Documentation détaillée de l'architecture
├── patient-service/            # Microservice de gestion des patients
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── mvnw.cmd
├── gateway/                    # API Gateway
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── mvnw.cmd
├── note-service/               # À venir - Sprint 2
├── risk-service/               # À venir - Sprint 3
└── frontend/                   # À venir - Interface utilisateur
```

## 🔄 Sprints

### Sprint 1 - ✅ Terminé
- [x] Architecture microservices avec Gateway
- [x] Microservice Patient avec MongoDB
- [x] Dockerisation
- [x] Données de test des 4 patients

### Sprint 2 - 🚧 En cours
- [ ] Microservice Note (MongoDB)
- [ ] Gestion des notes médicales
- [ ] Historique patient

### Sprint 3 - 📋 À faire
- [ ] Microservice Risk Assessment
- [ ] Évaluation du risque de diabète
- [ ] Règles métier pour les niveaux de risque

## 📝 User Stories

### Sprint 1
1. **Vue des infos personnelles des patients** ✅
   - En tant qu'organisateur, j'aimerais voir les informations personnelles de mes patients

2. **Mise à jour des informations personnelles** ✅
   - En tant qu'organisateur, j'aimerais mettre à jour les informations personnelles

3. **Ajouter des informations personnelles** ✅
   - En tant qu'organisateur, j'aimerais ajouter des informations personnelles

### Sprint 2
4. **Vue historique du patient** 🚧
   - En tant que praticien, je veux voir l'historique des informations de mon patient

5. **Ajouter une note à l'historique** 🚧
   - En tant que praticien, je veux pouvoir ajouter une note d'observation

### Sprint 3
6. **Générer un rapport de diabète** 📋
   - En tant que praticien, je veux pouvoir consulter le risque de diabète

## 🧪 Tests

### Tester l'ajout d'un patient
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jean",
    "lastName": "Dupont",
    "birthDate": "1990-05-15",
    "gender": "M",
    "address": "123 Rue de Paris",
    "phoneNumber": "01-23-45-67-89"
  }'
```

### Tester la mise à jour d'un patient
```bash
curl -X PUT http://localhost:8080/api/patients/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jean",
    "lastName": "Dupont",
    "birthDate": "1990-05-15",
    "gender": "M",
    "address": "456 Avenue des Champs",
    "phoneNumber": "01-98-76-54-32"
  }'
```

## 📖 Documentation Complémentaire

- [ARCHITECTURE.md](ARCHITECTURE.md) - Documentation détaillée de l'architecture
- [GATEWAY_README.md](GATEWAY_README.md) - Documentation du Gateway
- [PDF/](PDF/) - Documents de spécifications et cas de test

## 👥 Équipe

Projet Medilabo - OpenClassrooms DA Java

## 📄 Licence

Projet éducatif - OpenClassrooms

