package fr.emse.negotiation.contentnegotiation.violation;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.jena.graph.Graph;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;

import java.util.*;
import java.util.stream.Collectors;

public class ViolationCheckerImpl implements ViolationChecker {

    @Override
    public MutablePair<Long, Long> validateGraph(Graph dataGraph, Graph shapeGraph) {
        Shapes shapes = Shapes.parse(shapeGraph);
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        Long numberOfViolations = (long) report.getEntries().size();
        Long validatedConstrains = (long) report.getNumberOfValidatedConstraints();

        return new MutablePair<>(validatedConstrains, validatedConstrains - numberOfViolations);
    }

    @Override
    public Float calculateViolationMeasure(MutablePair<Long, Long> validatedValidPair, Float qualityValue) {
        Float q = ((float) validatedValidPair.right / (Math.max(1, validatedValidPair.left))) * qualityValue;
        return q;
    }

    @Override
    public HashMap<ImmutablePair<String, String>, Float> sortDescending(HashMap<ImmutablePair<String, String>, Float> map) {
        return map
                .entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    @Override
    public Map.Entry<ImmutablePair<String, String>, Float> firstInMap(HashMap<ImmutablePair<String, String>, Float> map) {
        return map.entrySet().stream().findFirst().orElse(null);
    }
}
