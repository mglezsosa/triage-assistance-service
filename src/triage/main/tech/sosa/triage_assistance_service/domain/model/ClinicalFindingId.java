package tech.sosa.triage_assistance_service.domain.model;

import java.util.Objects;

public class ClinicalFindingId {

    private String value;

    public ClinicalFindingId(String value) {
        this.value = value;
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
