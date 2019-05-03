package tech.sosa.triage_assistance_service.domain.model;

import java.util.Objects;

public class ChiefComplaint {

    private ChiefComplaintId id;

    public ChiefComplaint(ChiefComplaintId id) {
        this.id = id;
    }

    public ChiefComplaintId id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChiefComplaint that = (ChiefComplaint) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{ id: " + id + " }";
    }
}
