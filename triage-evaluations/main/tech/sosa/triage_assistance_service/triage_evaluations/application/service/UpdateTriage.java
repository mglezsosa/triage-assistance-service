package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;

public class UpdateTriage implements ApplicationService<Void, UpdateTriageRequest> {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;

    public UpdateTriage(TriageRepository triageRepo,
            TriageMapper triageMapper) {
        this.triageRepo = triageRepo;
        this.triageMapper = triageMapper;
    }

    public Void execute(UpdateTriageRequest request) {
        Triage updatedTriage = triageMapper.from(request.triage);

        checkTriageExists(updatedTriage);

        triageRepo.save(updatedTriage);

        return null;
    }

    private void checkTriageExists(Triage updatedTriage) {
        if (triageRepo.find(updatedTriage.chiefComplaint()) == null) {
            throw TriageDoesNotExistException.withChiefComplaint(updatedTriage.chiefComplaint());
        }
    }
}
