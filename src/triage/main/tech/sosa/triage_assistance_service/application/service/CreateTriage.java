package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.ApplicationService;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageAlreadyExistsException;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class CreateTriage implements ApplicationService<Void, CreateTriageRequest> {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;

    public CreateTriage(TriageRepository triageRepo,
            TriageMapper triageMapper) {
        this.triageRepo = triageRepo;
        this.triageMapper = triageMapper;
    }

    public Void execute(CreateTriageRequest request) {
        Triage newTriage = triageMapper.from(request.triage);

        checkNewTriageExists(newTriage);

        triageRepo.save(newTriage);

        return null;
    }

    private void checkNewTriageExists(Triage newTriage) {
        if (triageRepo.find(newTriage.chiefComplaint()) != null) {
            throw TriageAlreadyExistsException.withChiefComplaint(newTriage.chiefComplaint());
        }
    }
}
