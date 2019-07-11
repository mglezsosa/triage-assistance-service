package tech.sosa.triage_assistance_service.identity_access.domain.model;

import tech.sosa.triage_assistance_service.identity_access.domain.event.AuthorizationChecked;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;
import tech.sosa.triage_assistance_service.shared.domain.event.EventPublisher;

public class User {

    private String id;
    private Role role;

    public User(String id, Role role) {
        this.id = id;
        this.role = role;
    }

    public boolean hasAuthorization(String useCaseName, String request) {
        boolean isAuthorized = role.hasAuthorization(useCaseName);

        EventPublisher.instance().publish(new AuthorizationChecked(this, useCaseName, request, isAuthorized));

        return isAuthorized;
    }

    public Role getRole() {
        return role;
    }

    public String getId() {
        return id;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(String id) {
        this.id = id;
    }
}
