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
    private Long validatedConstraints = 0L;
    private Long notValidConstraintsInfo = 0L;
    private Long notValidConstraintsWarning = 0L;
    private Long notValidConstraintsViolation = 0L;

    @Override
    public int compareTo(ValidationEntry o) {
        int result = getViolationRatio().compareTo(o.getViolationRatio());
        if (result == 0) {
            result = getWarningRatio().compareTo(o.getWarningRatio());
            if (result == 0) {
                return getInfoRatio().compareTo(o.getInfoRatio());
            }
            return result;
        }
        return result;
    }

    public Float getViolationRatio() {
        return (float) (notValidConstraintsViolation + 1) / (Math.max(1, validatedConstraints) - (notValidConstraintsWarning + notValidConstraintsInfo));
    }

    public Float getWarningRatio() {
        return (float) (notValidConstraintsInfo + 1) / (validatedConstraints - (Math.max(1, notValidConstraintsViolation) + notValidConstraintsWarning));
    }

    public Float getInfoRatio() {
        return (float) (notValidConstraintsWarning + 1) / (validatedConstraints - (Math.max(1, notValidConstraintsViolation) + notValidConstraintsInfo));
    }

    @Override
    public String toString() {
        return "ValidationEntry{" +
                "shapeIRI='" + shapeIRI + '\'' +
                ", graphIRI='" + graphIRI + '\'' +
                ", validatedConstraints=" + validatedConstraints +
                ", notValidConstraintsInfo=" + notValidConstraintsInfo +
                ", notValidConstraintsWarning=" + notValidConstraintsWarning +
                ", notValidConstraintsViolation=" + notValidConstraintsViolation +
                '}';
    }
}
