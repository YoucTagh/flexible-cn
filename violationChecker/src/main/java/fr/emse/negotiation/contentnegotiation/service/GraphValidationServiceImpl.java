package fr.emse.negotiation.contentnegotiation.service;

import fr.emse.negotiation.contentnegotiation.model.ValidationEntry;
import fr.emse.negotiation.contentnegotiation.violation.ViolationChecker;
import fr.emse.negotiation.contentnegotiation.violation.ViolationCheckerImpl;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GraphValidationServiceImpl implements GraphValidationService {

    private final String rootGraphsURI = "https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h1/";

    private final ViolationChecker violationChecker = new ViolationCheckerImpl();

    @Override
    public ResponseEntity<String> getValidSimpleGraph(String shapePath, int testCase) {

        Graph shapesGraph = RDFDataMgr.loadGraph(shapePath);
        int minViolation = Integer.MAX_VALUE;
        String bestGraphURI = "";

        for (int i = 1; i <= 3; i++) {
            String graphURI = rootGraphsURI + "simple-data/" + testCase + "/data_graph_" + i + ".ttl";
            Graph dataGraph = RDFDataMgr.loadGraph(graphURI);
            ValidationReport report = validateGraph(dataGraph, shapesGraph);
            int numberViolations = report.getEntries().size();
            if (numberViolations < minViolation) {
                minViolation = numberViolations;
                bestGraphURI = graphURI;
            }
            System.out.println(graphURI + " : " + numberViolations);
        }

        //ShLib.printReport(report);

        //RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);

        ResponseEntity<String> response = new ResponseEntity<>(
                "{"
                        + "\"bestGraphURI\":" + "\"" + bestGraphURI + "\","
                        + "\"violationNumber\":" + minViolation
                        + "}"
                , HttpStatus.OK
        );
        return response;
    }
    private ValidationReport validateGraph(Graph dataGraph, Graph shapesGraph) {
        Shapes shapes = Shapes.parse(shapesGraph);
        return ShaclValidator.get().validate(shapes, dataGraph);
    }



    @Override
    public ResponseEntity<String> getValidComplexGraph(String shapePath) {

        Graph shapeGraph = RDFDataMgr.loadGraph(shapePath);
        HashMap<ImmutablePair<String, String>, Float> map = new HashMap<>();
        ArrayList<ValidationEntry> entryArrayList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String graphURI = rootGraphsURI + "complex-data/data_graph_" + i + ".ttl";
            Graph dataGraph = RDFDataMgr.loadGraph(graphURI);
            MutablePair<Long, Long> pair = violationChecker.validateGraph(dataGraph, shapeGraph);
            Float q = violationChecker.calculateViolationMeasure(pair, (float) Math.min(1.0, 1));
            System.out.println(graphURI + " : <" + pair.left + "," + pair.right + ">" + " => " + q);
            map.put(new ImmutablePair<>(graphURI, shapePath), q);
            entryArrayList.add(
                    new ValidationEntry()
                            .setGraphIRI(graphURI)
                            .setShapeIRI(shapePath)
                            .setValidatedConstraints(pair.left)
                            .setValidConstraints(pair.right)
                            .setQValue(q));
        }
        System.out.println();
        //HashMap
        HashMap<ImmutablePair<String, String>, Float> sortedMap = violationChecker.sortDescending(map);
        Map.Entry<ImmutablePair<String, String>, Float> bestEntry = violationChecker.firstInMap(sortedMap);

        /*
        ResponseEntity<String> response = new ResponseEntity<>(
                "{"
                        + "\"Data Graph URI\":" + "\"" + bestEntry.getKey().left + "\","
                        + "\"Shape Graph URI\":" + "\"" + bestEntry.getKey().right + "\","
                        + "\"Q-value\":" + bestEntry.getValue()
                        + "}"
                , HttpStatus.OK
        );
        */


        //HashSet
        Collections.sort(entryArrayList);
        ValidationEntry bestValidationEntry = entryArrayList.get(0);

        ResponseEntity<String> response = new ResponseEntity<>(
                "{"
                        + "\n\"Data Graph URI\":" + "\"" + bestValidationEntry.getGraphIRI() + "\","
                        + "\n\"Shape Graph URI\":" + "\"" + bestValidationEntry.getShapeIRI() + "\","
                        + "\n\"Validated constraints\":" + bestValidationEntry.getValidatedConstraints() + ","
                        + "\n\"Valid constraints\":" + bestValidationEntry.getValidConstraints() + ","
                        + "\n\"Q-value\":" + bestValidationEntry.getQValue()
                        + "\n}"
                , HttpStatus.OK
        );


        return response;
    }



}
