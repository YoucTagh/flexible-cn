package fr.emse.negotiation.contentnegotiation.service;

import org.springframework.http.ResponseEntity;

public interface GraphValidationService {
    ResponseEntity<String> getValidGraph(String shapePath);
}
