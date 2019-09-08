package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.NullRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;

public class ViewNumberOfEqueuedCases implements ApplicationService<Long, NullRequest> {
    private final PendingTriagesQueue queue;

    public ViewNumberOfEqueuedCases(PendingTriagesQueue queue) {
        this.queue = queue;
    }

    @Override
    public Long execute(NullRequest request) {
        return queue.numberOfEnqueuedCases();
    }
}
