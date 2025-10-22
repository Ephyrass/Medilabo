# Gateway - Spring Cloud Gateway (Medilabo)

This README provides a quick reference to the Gateway configuration and routing.

## Overview

The Gateway exposes port `8080` and routes client requests to backend microservices:
- `/api/patients/**` → Patient Service (http://patient-service:8081)
- `/api/notes/**` → Note Service (http://note-service:8082)
- `/api/assess/**` → Risk Service (http://risk-service:8083)

It includes Resilience4j circuit breakers, retries, and CORS configuration for frontend origins.

## Start locally

```bash
cd gateway
mvnw.cmd spring-boot:run
```

## Docker

Build and run:

```bash
cd gateway
mvnw.cmd clean package
docker build -t medilabo/gateway .
```

Health endpoint: `http://localhost:8080/actuator/health`

See `gateway/src/main/java/com/medilabo/gateway/config/GatewayConfig.java` for route definitions.

