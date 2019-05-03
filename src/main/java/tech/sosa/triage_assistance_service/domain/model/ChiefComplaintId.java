package tech.sosa.triage_assistance_service.domain.model;

import java.util.Objects;

public class ChiefComplaintId {

    private String value;

    public ChiefComplaintId(String value) {
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
        ChiefComplaintId that = (ChiefComplaintId) o;
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
