package tech.sosa.triage_assistance_service.triage_evaluations.domain.event;

import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.shared.domain.event.EventSubscriber;

public class NonCriticalAssessingEqueuerSubscriber implements
        EventSubscriber<CriticalCheckTriageAssessed> {

    private PendingTriagesQueue queue;

    public NonCriticalAssessingEqueuerSubscriber(
            PendingTriagesQueue queue) {
        this.queue = queue;
    }

    @Override
    public void handleEvent(CriticalCheckTriageAssessed anEvent) {
        if (anEvent.getOutput().hasCriticalState) {
            return;
        }
        queue.enqueue(anEvent);
    }

    @Override
    public Class<CriticalCheckTriageAssessed> subscribedToEventType() {
        return CriticalCheckTriageAssessed.class;
    }
}
