package tech.sosa.triage_assistance_service.infrastructure;

import java.util.stream.Stream;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

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
