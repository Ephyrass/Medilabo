package com.medilabo.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Fallback Controller - Gestion des erreurs de circuit breaker
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/patients")
    public Mono<Map<String, String>> patientsFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Patient Service indisponible");
        response.put("message", "Le service de gestion des patients est temporairement indisponible. Veuillez réessayer plus tard.");
        return Mono.just(response);
    }

    @GetMapping("/notes")
    public Mono<Map<String, String>> notesFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Note Service indisponible");
        response.put("message", "Le service de gestion des notes est temporairement indisponible. Veuillez réessayer plus tard.");
        return Mono.just(response);
    }

    @GetMapping("/risk")
    public Mono<Map<String, String>> riskFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Risk Service indisponible");
        response.put("message", "Le service d'évaluation des risques est temporairement indisponible. Veuillez réessayer plus tard.");
        return Mono.just(response);
    }
}

