package fr.emse.negotiation.contentnegotiation.violation;

import fr.emse.negotiation.contentnegotiation.model.ValidationEntry;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.validation.Severity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ViolationCheckerSeverityImpl implements ViolationChecker {

    @Override
    public ValidationEntry validateGraph(String dataGraphPath, String shapePath) {
        Graph shapeGraph = RDFDataMgr.loadGraph(shapePath);
        Graph dataGraph = RDFDataMgr.loadGraph(dataGraphPath);
        Shapes shapes = Shapes.parse(shapeGraph);

        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);

        ValidationEntry validationEntry = new ValidationEntry()
                .setGraphIRI(dataGraphPath)
                .setShapeIRI(shapePath);

        validationEntry.setValidatedConstraints((long) report.getNumberOfValidatedConstraints());

        report.getEntries().forEach(reportEntry -> {
            if (reportEntry.severity().equals(Severity.Violation)) {
                validationEntry.setNotValidConstraintsViolation(validationEntry.getNotValidConstraintsViolation() + 1);
            } else if (reportEntry.severity().equals(Severity.Warning)) {
                validationEntry.setNotValidConstraintsWarning(validationEntry.getNotValidConstraintsWarning() + 1);
            } else if (reportEntry.severity().equals(Severity.Info)) {
                validationEntry.setNotValidConstraintsInfo(validationEntry.getNotValidConstraintsInfo() + 1);
            }
        });


        return validationEntry;
    }

    @Override
    public ArrayList<ValidationEntry> sortDescending(ArrayList<ValidationEntry> list) {
        Collections.sort(list);
        return list;
    }

    @Override
    public ValidationEntry firstInMap(ArrayList<ValidationEntry> list) {
        return list.get(0);
    }
}
