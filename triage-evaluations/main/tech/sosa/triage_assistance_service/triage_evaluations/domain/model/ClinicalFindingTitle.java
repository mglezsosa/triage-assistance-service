package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import java.util.Objects;

public class ClinicalFindingTitle {

    private String value;

    public ClinicalFindingTitle(String value) {
        this.value = value;
    }

    public String value() {
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
        ClinicalFindingTitle that = (ClinicalFindingTitle) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
