package fr.emse.negotiation.contentnegotiation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ValidationEntry implements Comparable<ValidationEntry> {
    private String shapeIRI;
    private String graphIRI;
    private Long validatedConstraints;
    private Long validConstraints;
    private Float qValue;


    @Override
    public int compareTo(ValidationEntry o) {
        return o.getQValue().compareTo(qValue);
    }
}
