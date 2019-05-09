package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaintId;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

public class DeleteTriage {

    private final TriageRepository triageRepository;

    public DeleteTriage(TriageRepository triageRepository) {
        this.triageRepository = triageRepository;
    }

    public void execute(DeleteTriageRequest request) {

        ChiefComplaint reqTriageChiefComplaint = new ChiefComplaint(
                new ChiefComplaintId(request.chiefComplaintId)
        );
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
