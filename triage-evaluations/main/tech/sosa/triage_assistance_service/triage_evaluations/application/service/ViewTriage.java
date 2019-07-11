package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;

public class ViewTriage implements ApplicationService<TriageDTO, ViewTriageRequest> {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;

    public ViewTriage(TriageRepository triageRepo, TriageMapper triageMapper) {
        this.triageMapper = triageMapper;
        this.triageRepo = triageRepo;
    }

    public TriageDTO execute(ViewTriageRequest request) {

        ChiefComplaint reqTriageChiefComplaint = triageMapper.buildChiefComplaint(
                request.chiefComplaintId, null);

        Triage requestedTriage = triageRepo.find(reqTriageChiefComplaint);

        checkTriageExists(reqTriageChiefComplaint, requestedTriage);

        return triageMapper.to(requestedTriage);
    }

    private void checkTriageExists(ChiefComplaint reqTriageChiefComplaint, Triage requestedTriage) {
        if (null == requestedTriage) {
            throw TriageDoesNotExistException.withChiefComplaint(reqTriageChiefComplaint);
        }
    }
}
