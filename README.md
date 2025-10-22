# Medilabo - Patient Records Management Application

## 🏥 Description

Medilabo is a patient records management application based on a **microservices architecture**. It allows organizers and practitioners to manage patient information, medical notes, and assess diabetes risks.

## 🏗️ Microservices Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Client / Frontend                     │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP Requests
                         │ http://localhost:8080
                         ▼
┌─────────────────────────────────────────────────────────┐
│           Spring Cloud Gateway (Port 8080)              │
│  - Smart Routing                                         │
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
Patient records management with MongoDB.

**REST Endpoints:**
- `GET /api/patients` - List all patients
- `GET /api/patients/{id}` - Get patient details
- `POST /api/patients` - Add a patient
- `PUT /api/patients/{id}` - Update a patient
- `DELETE /api/patients/{id}` - Delete a patient

**Sprint 1 User Stories:**
- ✅ View patient personal information
- ✅ Update personal information
- ✅ Add patient personal information

### 2. Gateway (Port 8080) - ✅ Sprint 1
Single entry point with Spring Cloud Gateway.

**Features:**
- Routing to microservices
- Circuit Breaker with Resilience4j
- Automatic retry (3 attempts)
- CORS configuration
- Fallback on error

### 3. Note Service (Port 8082) - 🚧 Sprint 2
Medical notes management with MongoDB (coming soon).

### 4. Risk Assessment Service (Port 8083) - 🚧 Sprint 3
Diabetes risk assessment (coming soon).

### 5. Frontend (Port 80 / 3000) - ✅ Sprint 1
Vue 3 + Vite + Tailwind CSS user interface.

**Features:**
- List all patients
- Responsive design with Tailwind CSS
- Development mode with hot reload (Vite)
- Production build with nginx
- API calls proxied to Gateway

## 🚀 Quick Start

### Prerequisites
- Java 17
- Maven
- Docker & Docker Compose
- MongoDB
- Node.js 18+ (for local frontend development)

### Option 1: Run everything with Docker Compose (Recommended)

```bash
docker-compose up -d
```

This will start:
- MongoDB on port 27017
- Patient Service on port 8081
- Gateway on port 8080
- Frontend on port 80 (http://localhost)

Access the application at **http://localhost**

### Option 2: Run services individually

#### 1. Start MongoDB
```bash
docker-compose up -d mongodb
```

#### 2. Start Patient Service
```bash
cd patient-service
mvnw.cmd spring-boot:run
```

#### 3. Start Gateway
```bash
cd gateway
mvnw.cmd spring-boot:run
```

#### 4. Start Frontend (development mode)
```bash
cd frontend
npm install
npm run dev
```

Access the application at **http://localhost:3000**

### 4. Test the API
```bash
# Via Gateway (recommended)
curl http://localhost:8080/api/patients

# Directly to Patient Service
curl http://localhost:8081/api/patients
```

## 📊 Test Data

The application automatically loads 4 test patients on startup:

1. **TestNone Test** (F, 58 years old) - No risk
2. **TestBorderline Test** (M, 79 years old) - Borderline risk
3. **TestInDanger Test** (M, 21 years old) - In danger
4. **TestEarlyOnset Test** (F, 23 years old) - Early onset

## 🐳 Docker

Each microservice has its own Dockerfile for containerization.

### Build Docker images
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

### Launch all services with Docker Compose
```bash
docker-compose up -d
```

## 🛠️ Technologies Used

- **Backend:** Spring Boot 3.5.6, Java 17
- **API Gateway:** Spring Cloud Gateway
- **Database:** MongoDB 6.0
- **Resilience:** Resilience4j (Circuit Breaker)
- **Build:** Maven
- **Containerization:** Docker

## 📁 Project Structure

```
Medilabo/
├── docker-compose.yml          # Docker orchestration
├── README.md                   # This file
├── ARCHITECTURE.md             # Detailed architecture documentation
├── patient-service/            # Patient management microservice
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── mvnw.cmd
├── gateway/                    # API Gateway
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── mvnw.cmd
├── note-service/               # Coming soon - Sprint 2
├── risk-service/               # Coming soon - Sprint 3
└── frontend/                   # Coming soon - User interface
```

## 🔄 Sprints

### Sprint 1 - ✅ Completed
- [x] Microservices architecture with Gateway
- [x] Patient Microservice with MongoDB
- [x] Dockerization
- [x] Test data for 4 patients

### Sprint 2 - 🚧 In Progress
- [ ] Note Microservice (MongoDB)
- [ ] Medical notes management
- [ ] Patient history

### Sprint 3 - 📋 To Do
- [ ] Risk Assessment Microservice
- [ ] Diabetes risk assessment
- [ ] Business rules for risk levels

## 📝 User Stories

### Sprint 1
1. **View patient personal information** ✅
   - As an organizer, I would like to view my patients' personal information

2. **Update personal information** ✅
   - As an organizer, I would like to update personal information

3. **Add patient personal information** ✅
   - As an organizer, I would like to add patient personal information

### Sprint 2
4. **View patient history** 🚧
   - As a practitioner, I want to see my patient's information history

5. **Add note to history** 🚧
   - As a practitioner, I want to add an observation note

### Sprint 3
6. **Generate diabetes report** 📋
   - As a practitioner, I want to view diabetes risk

## 🧪 Tests

### Test adding a patient
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1990-05-15",
    "gender": "M",
    "address": "123 Paris Street",
    "phoneNumber": "01-23-45-67-89"
  }'
```

### Test updating a patient
```bash
curl -X PUT http://localhost:8080/api/patients/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1990-05-15",
    "gender": "M",
    "address": "456 Champs Avenue",
    "phoneNumber": "01-98-76-54-32"
  }'
```

## 📖 Additional Documentation

- ARCHITECTURE.md — Detailed architecture documentation
- gateway/README.md — Gateway documentation (see gateway/README.md)
- PDF/ — Specifications and test case documents

## 👥 Team

Medilabo Project - OpenClassrooms Java DA

## 📄 License

Educational Project - OpenClassrooms
