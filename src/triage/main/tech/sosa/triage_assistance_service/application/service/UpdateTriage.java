package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class UpdateTriage {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;

    public UpdateTriage(TriageRepository triageRepo,
            TriageMapper triageMapper) {
        this.triageRepo = triageRepo;
        this.triageMapper = triageMapper;
    }

    public void execute(UpdateTriageRequest request) {
        Triage updatedTriage = triageMapper.from(request.triageDTO);

        checkTriageExists(updatedTriage);

        triageRepo.save(updatedTriage);
    }

    private void checkTriageExists(Triage updatedTriage) {
        if (triageRepo.find(updatedTriage.chiefComplaint()) == null) {
            throw TriageDoesNotExistException.withChiefComplaint(updatedTriage.chiefComplaint());
        }
    }
}
