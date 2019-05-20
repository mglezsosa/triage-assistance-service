package tech.sosa.triage_assistance_service.domain.model;

import java.util.Objects;
import org.snomed.languages.scg.domain.model.Expression;

public class ClinicalFindingId {

    private Expression value;

    public ClinicalFindingId(Expression value) {
        this.value = value;
    }

    public Expression value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClinicalFindingId that = (ClinicalFindingId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ClinicalFindingId{" +
                "value=" + value +
                '}';
    }
}
