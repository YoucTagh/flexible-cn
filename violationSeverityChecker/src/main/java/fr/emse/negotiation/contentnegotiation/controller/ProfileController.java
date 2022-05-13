package fr.emse.negotiation.contentnegotiation.controller;

import fr.emse.negotiation.contentnegotiation.service.GraphValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fr/emse/kg/negotiation")
public class ProfileController {

    private final GraphValidationService graphValidationService;

    public ProfileController(GraphValidationService graphValidationService) {
        this.graphValidationService = graphValidationService;
    }

    @RequestMapping("/severity")
    public ResponseEntity<String> getComplexValidGraph(@RequestHeader("Accept-profile") String profile) {
        return graphValidationService.getValidGraph(profile);
    }

}
