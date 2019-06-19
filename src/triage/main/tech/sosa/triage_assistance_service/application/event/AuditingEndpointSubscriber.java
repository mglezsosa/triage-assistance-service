package tech.sosa.triage_assistance_service.application.event;

import tech.sosa.triage_assistance_service.util.event.EventStore;
import tech.sosa.triage_assistance_service.util.event.EventSubscriber;

public class AuditingEndpointSubscriber implements EventSubscriber<AuthenticatedEndpointReached> {

    private EventStore eventStore;

    public AuditingEndpointSubscriber(
            EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handleEvent(AuthenticatedEndpointReached anEvent) {
        this.eventStore.store(anEvent);
    }

    @Override
    public Class<AuthenticatedEndpointReached> subscribedToEventType() {
        return AuthenticatedEndpointReached.class;
    }
}
