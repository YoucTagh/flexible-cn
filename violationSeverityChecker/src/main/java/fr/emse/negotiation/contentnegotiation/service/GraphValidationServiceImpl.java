package fr.emse.negotiation.contentnegotiation.service;

import fr.emse.negotiation.contentnegotiation.model.ValidationEntry;
import fr.emse.negotiation.contentnegotiation.violation.ViolationChecker;
import fr.emse.negotiation.contentnegotiation.violation.ViolationCheckerSeverityImpl;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class GraphValidationServiceImpl implements GraphValidationService {

    private final String rootGraphsURI = "https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/";

    private final ViolationChecker violationChecker = new ViolationCheckerSeverityImpl();


    private ValidationReport validateGraph(Graph dataGraph, Graph shapesGraph) {
        Shapes shapes = Shapes.parse(shapesGraph);
        return ShaclValidator.get().validate(shapes, dataGraph);
    }

    @Override
    public ResponseEntity<String> getValidGraph(String shapePath) {

        ArrayList<ValidationEntry> entryArrayList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String dataGraphPath = rootGraphsURI + "data_graph_" + i + ".ttl";
            entryArrayList.add(violationChecker.validateGraph(dataGraphPath, shapePath));
        }

        ArrayList<ValidationEntry> sortedList = violationChecker.sortDescending(entryArrayList);

        sortedList.forEach(System.out::println);

        System.out.println("----------------------------");

        ValidationEntry bestEntry = violationChecker.firstInMap(sortedList);
        System.out.println(bestEntry);

        ResponseEntity<String> response = new ResponseEntity<>(
                "{"
                        + "\n\"Data Graph URI\":" + "\"" + bestEntry.getGraphIRI() + "\","
                        + "\n\"Shape Graph URI\":" + "\"" + bestEntry.getShapeIRI() + "\","
                        + "\n\"Validated constraints \":" + bestEntry.getValidatedConstraints() + ","
                        + "\n\"Not Valid constraints Violation\":" + bestEntry.getNotValidConstraintsViolation() + ","
                        + "\n\"Not Valid constraints Warning\":" + bestEntry.getNotValidConstraintsWarning() + ","
                        + "\n\"Not Valid constraints Info\":" + bestEntry.getNotValidConstraintsInfo() + ","
                        + "\n}"
                , HttpStatus.OK
        );


        return response;
    }


}
