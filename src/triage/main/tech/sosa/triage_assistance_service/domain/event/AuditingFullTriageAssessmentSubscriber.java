package tech.sosa.triage_assistance_service.domain.event;

import tech.sosa.triage_assistance_service.util.event.EventStore;
import tech.sosa.triage_assistance_service.util.event.EventSubscriber;

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
