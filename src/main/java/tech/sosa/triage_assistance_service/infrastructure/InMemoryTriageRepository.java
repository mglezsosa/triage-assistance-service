package tech.sosa.triage_assistance_service.infrastructure;

import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class InMemoryTriageRepository implements TriageRepository {

    @Override
    public Triage find(ChiefComplaint complaint) {
        return null;
    }

    @Override
    public void save(Triage triage) {

    }
}
