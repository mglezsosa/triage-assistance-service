package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.NullRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;

public class NextEnqueuedTriage implements
        ApplicationService<CriticalCheckTriageAssessed, NullRequest> {

    private PendingTriagesQueue queue;

    public NextEnqueuedTriage(PendingTriagesQueue queue) {
        this.queue = queue;
    }

    @Override
    public CriticalCheckTriageAssessed execute(NullRequest request) {
        return queue.nextPending();
    }
}
