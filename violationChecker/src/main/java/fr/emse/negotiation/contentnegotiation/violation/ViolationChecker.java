package fr.emse.negotiation.contentnegotiation.violation;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.jena.graph.Graph;

import java.util.HashMap;
import java.util.Map;

public interface ViolationChecker {
    MutablePair<Long, Long> validateGraph(Graph dataGraph, Graph shapeGraph);

    Float calculateViolationMeasure(MutablePair<Long, Long> validatedValidPair, Float qualityValue);

    HashMap<ImmutablePair<String, String>, Float> sortDescending(HashMap<ImmutablePair<String, String>, Float> map);

    Map.Entry<ImmutablePair<String, String>, Float> firstInMap(HashMap<ImmutablePair<String, String>, Float> map);
}
