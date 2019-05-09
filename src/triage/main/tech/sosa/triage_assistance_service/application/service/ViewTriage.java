package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaintId;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class ViewTriage {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;

    public ViewTriage(TriageRepository triageRepo, TriageMapper triageMapper) {
        this.triageMapper = triageMapper;
        this.triageRepo = triageRepo;
    }

    public TriageDTO execute(ViewTriageRequest request) {

        ChiefComplaint reqTriageChiefComplaint = new ChiefComplaint(
                new ChiefComplaintId(request.chiefComplaintId)
        );

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
