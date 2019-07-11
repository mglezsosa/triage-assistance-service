package tech.sosa.triage_assistance_service.triage_evaluations.persistence;

import java.util.stream.Stream;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;

public class TriageRepositoryStub {

    public static TriageRepository empty() {
        return new InMemoryTriageRepository();
    }

    public static TriageRepository with(Triage... triages) {
        TriageRepository repo = new InMemoryTriageRepository();
        Stream.of(triages).forEach(repo::save);
        return repo;
    }

}
