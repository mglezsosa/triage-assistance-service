package tech.sosa.triage_assistance_service.infrastructure;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class InMemoryTriageRepository implements TriageRepository {

    private Map<ChiefComplaint, Triage> triages;

    public InMemoryTriageRepository(
            Map<ChiefComplaint, Triage> triages) {
        this.triages = triages;
    }

    public InMemoryTriageRepository() {
        this(new HashMap<>());
    }

    @Override
    public Triage find(ChiefComplaint complaint) {
        return triages.get(complaint);
    }

    @Override
    public void save(Triage triage) {
        triages.put(triage.chiefComplaint(), triage);
    }

    @Override
    public void delete(Triage triage) {
        triages.remove(triage.chiefComplaint());
    }

    @Override
    public Collection<Triage> all() {
        return Collections.unmodifiableCollection(triages.values());
    }
}
