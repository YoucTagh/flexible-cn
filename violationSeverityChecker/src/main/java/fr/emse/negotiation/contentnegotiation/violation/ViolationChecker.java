package fr.emse.negotiation.contentnegotiation.violation;

import fr.emse.negotiation.contentnegotiation.model.ValidationEntry;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;

public interface ViolationChecker {
    ValidationEntry validateGraph(String dataGraphPath, String shapePath);
    
    ArrayList<ValidationEntry> sortDescending(ArrayList<ValidationEntry> map);

    ValidationEntry firstInMap(ArrayList<ValidationEntry> map);
}
