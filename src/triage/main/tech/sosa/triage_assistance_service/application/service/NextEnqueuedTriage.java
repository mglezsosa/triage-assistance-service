package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.ApplicationService;
import tech.sosa.triage_assistance_service.application.NullRequest;
import tech.sosa.triage_assistance_service.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.domain.model.PendingTriagesQueue;

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
