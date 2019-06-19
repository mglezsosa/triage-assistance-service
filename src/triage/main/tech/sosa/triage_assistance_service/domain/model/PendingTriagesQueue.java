package tech.sosa.triage_assistance_service.domain.model;

import tech.sosa.triage_assistance_service.domain.event.CriticalCheckTriageAssessed;

public interface PendingTriagesQueue {

    void enqueue(CriticalCheckTriageAssessed assessing);
    CriticalCheckTriageAssessed findInProcessOrFail(String id);
    CriticalCheckTriageAssessed nextPending();
    void finish(CriticalCheckTriageAssessed assessing);

}
