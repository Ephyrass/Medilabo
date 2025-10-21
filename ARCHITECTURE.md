# Architecture Medilabo avec Spring Cloud Gateway

## 📐 Vue d'ensemble

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend Client                       │
│              (React/Angular/Postman)                     │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP Requests
                         │ http://localhost:8080
                         ▼
┌─────────────────────────────────────────────────────────┐
│           Spring Cloud Gateway (Port 8080)              │
│  ┌────────────────────────────────────────────────┐     │
│  │  Gateway Config                                │     │
│  │  - Route Definitions                           │     │
│  │  - Circuit Breaker (Resilience4j)             │     │
│  │  - Retry Logic (3 attempts)                   │     │
│  │  - CORS Configuration                          │     │
│  │  - Global Logging Filter                      │     │
│  └────────────────────────────────────────────────┘     │
└─────┬───────────────────┬───────────────────┬──────────┘
      │                   │                   │
      │ /api/patients/**  │ /api/notes/**    │ /api/risk/**
      ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│   Patient    │   │     Note     │   │     Risk     │
│   Service    │   │   Service    │   │  Assessment  │
│              │   │              │   │   Service    │
│  Port 8081   │   │  Port 8082   │   │  Port 8083   │
│              │   │              │   │              │
│  In-Memory   │   │   MongoDB    │   │  Sprint 3    │
│  Storage     │   │  Database    │   │  (à venir)   │
└──────────────┘   └──────┬───────┘   └──────────────┘
                          │
                          ▼
                   ┌──────────────┐
                   │   MongoDB    │
                   │ Port 27017   │
                   └──────────────┘
```

## 🎯 Composants créés

### 1. Gateway Configuration (`GatewayConfig.java`)
- **Routage intelligent** vers les 3 microservices
- **Circuit Breaker** pour chaque service avec fallback
- **Retry automatique** : 3 tentatives en cas d'échec

### 2. CORS Configuration (`CorsConfig.java`)
- Permet les requêtes depuis React (3000), Angular (4200)
- Autorise tous les headers et méthodes HTTP
- Active les credentials (cookies, JWT)

### 3. Fallback Controller (`FallbackController.java`)
- `/fallback/patients` - Message si Patient Service down
- `/fallback/notes` - Message si Note Service down  
- `/fallback/risk` - Message si Risk Service down

### 4. Logging Filter (`LoggingFilter.java`)
- Log chaque requête : méthode + path
- Log chaque réponse : status + durée d'exécution
- Aide au debugging et monitoring

## 🔀 Table de routage

| Client Request | Gateway Route | Target Service |
|----------------|---------------|----------------|
| `GET /api/patients` | → `/api/patients/**` | Patient Service (8081) |
| `GET /api/notes` | → `/api/notes/**` | Note Service (8082) |
| `POST /api/notes` | → `/api/notes/**` | Note Service (8082) |
| `GET /api/risk/1` | → `/api/risk/**` | Risk Service (8083) |

## 🛡️ Résilience

### Circuit Breaker (Resilience4j)
```
Configuration pour chaque service :
- slidingWindowSize: 10 requêtes
- failureRateThreshold: 50% d'échecs = circuit ouvert
- waitDurationInOpenState: 10 secondes avant retry
```

### États du Circuit Breaker
1. **CLOSED** (Normal) : Toutes les requêtes passent
2. **OPEN** (Erreur) : Toutes les requêtes → fallback
3. **HALF_OPEN** (Test) : Quelques requêtes pour tester

## 📊 Exemple de flux

```
1. Client → GET http://localhost:8080/api/notes/patient/1

2. Gateway reçoit la requête
   - LoggingFilter enregistre : "Gateway Request: GET /api/notes/patient/1"

3. Gateway route vers Note Service
   - Match rule: /api/notes/**
   - Target: http://localhost:8082/api/notes/patient/1

4. Circuit Breaker vérifie l'état
   - Si CLOSED → Forward la requête
   - Si OPEN → Retourne fallback

5. Retry si échec
   - Tentative 1 : Échec
   - Tentative 2 : Échec
   - Tentative 3 : Succès ✓

6. Réponse retournée au client
   - LoggingFilter enregistre : "Gateway Response: 200 - 45ms"
```

## 🚀 Démarrage

### Option A : Mode monolithique (actuel)
```bash
# Démarrer MongoDB
docker run -d -p 27017:27017 mongo

# Démarrer l'application (Gateway + Services intégrés)
mvnw.cmd spring-boot:run
```
Tous les services sont dans la même application sur le port 8080.

### Option B : Mode microservices (recommandé pour production)
Séparer en 4 projets distincts :
1. medilabo-gateway (8080)
2. medilabo-patient (8081)  
3. medilabo-notes (8082)
4. medilabo-risk (8083)

## 🧪 Tests

### Test direct (sans Gateway)
```bash
# Patient Service
curl http://localhost:8081/api/patients

# Note Service  
curl http://localhost:8082/api/notes
```

### Test via Gateway
```bash
# Via Gateway - Patients
curl http://localhost:8080/api/patients

# Via Gateway - Notes
curl http://localhost:8080/api/notes

# Vérifier les logs du Gateway
# Vous verrez : 
# INFO Gateway Request: GET /api/patients
# INFO Gateway Response: GET /api/patients - Status: 200 - Duration: 23ms
```

### Test du Circuit Breaker
```bash
# Arrêter MongoDB (simuler une panne)
docker stop mongodb

# Appeler via Gateway
curl http://localhost:8080/api/notes

# Réponse du fallback :
# {
#   "error": "Note Service is temporarily unavailable",
#   "message": "Please try again later",
#   "service": "note-service"
# }
```

## 📈 Avantages du Gateway

✅ **Point d'entrée unique** - Un seul port pour tous les services
✅ **Résilience** - Circuit breaker + retry automatique
✅ **CORS centralisé** - Configuration unique
✅ **Monitoring** - Logs de toutes les requêtes
✅ **Sécurité** - Authentification centralisée (à ajouter)
✅ **Load balancing** - Peut router vers plusieurs instances
✅ **Rate limiting** - Limitation du nombre de requêtes (à ajouter)

## 🔜 Prochaines étapes

1. ✅ Sprint 1 : Patient Service (en mémoire)
2. ✅ Sprint 2 : Note Service (MongoDB)
3. ✅ Gateway : Spring Cloud Gateway avec Circuit Breaker
4. ⏳ Sprint 3 : Risk Assessment Service
   - Analyser les notes pour termes déclencheurs
   - Calculer le niveau de risque
   - Retourner : None, Borderline, In Danger, Early Onset

## 🎓 Concepts clés

- **API Gateway** : Point d'entrée unique
- **Circuit Breaker** : Protection contre les pannes en cascade
- **Fallback** : Réponse alternative en cas d'erreur
- **Retry** : Nouvelles tentatives automatiques
- **CORS** : Cross-Origin Resource Sharing
- **Reactive** : Spring WebFlux (non-bloquant)

