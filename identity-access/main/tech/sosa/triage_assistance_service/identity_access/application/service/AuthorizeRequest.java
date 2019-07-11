package tech.sosa.triage_assistance_service.identity_access.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;

public class AuthorizeRequest implements ApplicationRequest {

    public String useCaseName;
    public String authData;
    public String request;

    public AuthorizeRequest(String authData, String useCaseName, String request) {
        this.authData = authData;
        this.useCaseName = useCaseName;
        this.request = request;
    }

    public AuthorizeRequest() {
    }
}
