package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageAlreadyExistsException;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class CreateTriage {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;

    public CreateTriage(TriageRepository triageRepo,
            TriageMapper triageMapper) {
        this.triageRepo = triageRepo;
        this.triageMapper = triageMapper;
    }

    public void execute(CreateTriageRequest request) {
        Triage newTriage = triageMapper.from(request.triage);

        checkNewTriageExists(newTriage);

        triageRepo.save(newTriage);
    }

    private void checkNewTriageExists(Triage newTriage) {
        if (triageRepo.find(newTriage.chiefComplaint()) != null) {
            throw TriageAlreadyExistsException.withChiefComplaint(newTriage.chiefComplaint());
        }
    }
}
