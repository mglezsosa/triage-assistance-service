package tech.sosa.triage_assistance_service.triage_evaluations.domain.event;

import tech.sosa.triage_assistance_service.shared.domain.event.EventStore;
import tech.sosa.triage_assistance_service.shared.domain.event.EventSubscriber;

public class AuditingFullTriageAssessmentSubscriber implements EventSubscriber<FullTriageAssessed> {

    private EventStore eventStore;

    public AuditingFullTriageAssessmentSubscriber(
            EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handleEvent(FullTriageAssessed anEvent) {
        eventStore.store(anEvent);
    }

    @Override
    public Class<FullTriageAssessed> subscribedToEventType() {
        return FullTriageAssessed.class;
    }
}
