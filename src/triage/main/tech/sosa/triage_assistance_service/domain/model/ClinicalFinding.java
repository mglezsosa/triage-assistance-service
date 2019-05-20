package tech.sosa.triage_assistance_service.domain.model;

import java.util.Objects;

public class ClinicalFinding {

    protected ClinicalFindingId id;
    protected ClinicalFindingTitle title;

    public ClinicalFinding(ClinicalFindingId id,
            ClinicalFindingTitle title) {
        this.id = id;
        this.title = title;
    }

    public ClinicalFindingId id() {
        return id;
    }

    public ClinicalFindingTitle title() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClinicalFinding that = (ClinicalFinding) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{ id: " + id.toString() + " }";
    }
}
