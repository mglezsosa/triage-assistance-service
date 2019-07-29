package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import java.util.stream.Collectors;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.shared.domain.event.EventPublisher;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.PendingCasesQueueSizeChanged;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.*;

public class CheckForCriticalState
        implements ApplicationService<CriticalCheckAssesmentOutput, CheckForCriticalStateRequest> {

    private final TriageRepository repository;
    private final PendingTriagesQueue queue;

    public CheckForCriticalState(TriageRepository repository, PendingTriagesQueue queue) {
        this.repository = repository;
        this.queue = queue;
    }

    public CriticalCheckAssesmentOutput execute(CheckForCriticalStateRequest request) {
        ChiefComplaint selectedChiefComplaint = new ChiefComplaint(
                new ClinicalFindingId(request.triageChiefComplaintId), null);

        Triage selectedTriage = repository.find(selectedChiefComplaint);

        if (null == selectedTriage) {
            throw TriageDoesNotExistException.withChiefComplaint(selectedChiefComplaint);
        }

        CriticalCheckAssesmentOutput output = selectedTriage.checkForCriticalState(
                request.findingIds.parallelStream().map(fStr -> new ClinicalFinding(
                        new ClinicalFindingId(fStr), null)).collect(Collectors.toList())
        );

        if (!output.hasCriticalState) {
            queue.enqueue(CriticalCheckTriageAssessed.create(selectedTriage.chiefComplaint(), output));
            EventPublisher.instance().publish(new PendingCasesQueueSizeChanged());
        }

        return output;
    }

}
