package fr.emse.negotiation.contentnegotiation.controller;

import fr.emse.negotiation.contentnegotiation.service.GraphValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fr/emse/kg/negotiation/h1")
public class ProfileController {

    private final GraphValidationService graphValidationService;

    public ProfileController(GraphValidationService graphValidationService) {
        this.graphValidationService = graphValidationService;
    }

    @RequestMapping("/simple-data/1")
    public ResponseEntity<String> getSimpleValidGraph(@RequestHeader("Accept-profile") String profile) {
        return graphValidationService.getValidSimpleGraph(profile,1);
    }

    @RequestMapping("/simple-data/2")
    public ResponseEntity<String> getSimpleValidFailGraph(@RequestHeader("Accept-profile") String profile) {
        return graphValidationService.getValidSimpleGraph(profile,2);
    }

    @RequestMapping("/complex-data")
    public ResponseEntity<String> getComplexValidGraph(@RequestHeader("Accept-profile") String profile) {
        return graphValidationService.getValidComplexGraph(profile);
    }

}
