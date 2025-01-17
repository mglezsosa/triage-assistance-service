package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import java.util.Collection;

public interface TriageRepository {

    Triage find(ChiefComplaint complaint);

    void save(Triage triage);

    void delete(Triage triage);

    Collection<Triage> all();

}
