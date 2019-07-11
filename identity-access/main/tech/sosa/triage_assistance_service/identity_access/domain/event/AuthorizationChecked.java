package tech.sosa.triage_assistance_service.identity_access.domain.event;

import tech.sosa.triage_assistance_service.identity_access.domain.model.User;
import tech.sosa.triage_assistance_service.shared.domain.event.Event;

public class AuthorizationChecked extends Event {

    public final User user;
    public final String useCaseReached;
    public final String request;
    public final boolean wasAuthorized;

    public AuthorizationChecked(
            User user,
            String useCaseReached,
            String request,
            boolean wasAuthorized) {
        this.user = user;
        this.useCaseReached = useCaseReached;
        this.request = request;
        this.wasAuthorized = wasAuthorized;
    }
}
