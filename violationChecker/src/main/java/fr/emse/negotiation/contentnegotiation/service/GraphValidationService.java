package fr.emse.negotiation.contentnegotiation.service;

import org.springframework.http.ResponseEntity;

public interface GraphValidationService {
    ResponseEntity<String> getValidSimpleGraph(String shapePath,int testCase);
    ResponseEntity<String> getValidComplexGraph(String shapePath);
}
