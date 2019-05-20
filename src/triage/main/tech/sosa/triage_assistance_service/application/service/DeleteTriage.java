package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class DeleteTriage {

    private final TriageRepository triageRepository;
    private final TriageMapper mapper;

    public DeleteTriage(TriageRepository triageRepository, TriageMapper mapper) {
        this.triageRepository = triageRepository;
        this.mapper = mapper;
    }

    public void execute(DeleteTriageRequest request) {

        ChiefComplaint reqTriageChiefComplaint = mapper.buildChiefComplaint(
                request.chiefComplaintId, null);

        Triage requestedTriage = triageRepository.find(reqTriageChiefComplaint);

        checkTriageExists(reqTriageChiefComplaint, requestedTriage);

        triageRepository.delete(requestedTriage);
    }

    private void checkTriageExists(ChiefComplaint reqTriageChiefComplaint, Triage requestedTriage) {
        if (null == requestedTriage) {
            throw TriageDoesNotExistException.withChiefComplaint(reqTriageChiefComplaint);
        }
    }
}
