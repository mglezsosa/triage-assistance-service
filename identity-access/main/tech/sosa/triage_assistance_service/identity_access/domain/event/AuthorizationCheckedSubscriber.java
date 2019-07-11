package tech.sosa.triage_assistance_service.identity_access.domain.event;

import tech.sosa.triage_assistance_service.shared.domain.event.EventStore;
import tech.sosa.triage_assistance_service.shared.domain.event.EventSubscriber;

public class AuthorizationCheckedSubscriber implements EventSubscriber<AuthorizationChecked> {

    private EventStore<? super AuthorizationChecked> eventStore;

    public AuthorizationCheckedSubscriber(
            EventStore<? super AuthorizationChecked> eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handleEvent(AuthorizationChecked anEvent) {
        this.eventStore.store(anEvent);
    }

    @Override
    public Class<AuthorizationChecked> subscribedToEventType() {
        return AuthorizationChecked.class;
    }
}
