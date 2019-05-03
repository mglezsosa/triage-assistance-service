package tech.sosa.triage_assistance_service.domain.model;

public interface TriageRepository {

    Triage find(ChiefComplaint complaint);

    void save(Triage triage);

}
