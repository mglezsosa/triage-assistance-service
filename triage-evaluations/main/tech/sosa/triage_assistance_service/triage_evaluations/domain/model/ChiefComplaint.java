package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

public class ChiefComplaint extends ClinicalFinding {

    public ChiefComplaint(ClinicalFindingId id,
            ClinicalFindingTitle title) {
        super(id, title);
    }

    @Override
    public String toString() {
        return "ChiefComplaint{" +
                "id=" + id +
                ", title=" + title +
                '}';
    }
}
