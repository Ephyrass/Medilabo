# Note Service

Microservice pour la gestion des notes d'observation des patients (Sprint 2).

## Fonctionnalités

- Consulter l'historique des notes d'un patient
- Ajouter une nouvelle note d'observation
- Modifier une note existante
- Supprimer une note

## Technologies

- Spring Boot 3.2.0
- Spring Data MongoDB
- Java 17
- Lombok

## Endpoints

- `GET /api/notes/patient/{patientId}` - Récupérer toutes les notes d'un patient
- `POST /api/notes` - Créer une nouvelle note
- `PUT /api/notes/{noteId}` - Modifier une note
- `DELETE /api/notes/{noteId}` - Supprimer une note

## Configuration

Le service utilise MongoDB pour stocker les notes. Voir `application.properties` pour la configuration.

## Lancement

```bash
mvn spring-boot:run
```

Le service démarre sur le port 8082.
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <groupId>com.medilabo</groupId>
    <artifactId>note-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>note-service</name>
    <description>Microservice for managing patient notes</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Data MongoDB -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

