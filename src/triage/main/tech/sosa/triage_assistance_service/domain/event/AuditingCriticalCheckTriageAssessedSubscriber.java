package tech.sosa.triage_assistance_service.domain.event;

import tech.sosa.triage_assistance_service.util.event.EventStore;
import tech.sosa.triage_assistance_service.util.event.EventSubscriber;

public class AuditingCriticalCheckTriageAssessedSubscriber implements EventSubscriber<CriticalCheckTriageAssessed> {

    private EventStore eventStore;

    public AuditingCriticalCheckTriageAssessedSubscriber(
            EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handleEvent(CriticalCheckTriageAssessed anEvent) {
        eventStore.store(anEvent);
    }

    @Override
    public Class<CriticalCheckTriageAssessed> subscribedToEventType() {
        return CriticalCheckTriageAssessed.class;
    }
}
