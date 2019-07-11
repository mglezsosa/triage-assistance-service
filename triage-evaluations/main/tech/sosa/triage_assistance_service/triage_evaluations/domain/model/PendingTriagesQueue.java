package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;

public interface PendingTriagesQueue {

    void enqueue(CriticalCheckTriageAssessed assessing);
    CriticalCheckTriageAssessed findInProcessOrFail(String id);
    CriticalCheckTriageAssessed nextPending();
    void finish(CriticalCheckTriageAssessed assessing);

}
