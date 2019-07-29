package tech.sosa.triage_assistance_service.applications.application;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;

public class SecuredApplicationService<S, T extends ApplicationRequest> implements ApplicationService<S, T> {

    private ApplicationService<S, T> proxiedApplicationService;
    private AuthService authService;
    private AuthData authData;

    public SecuredApplicationService(
            ApplicationService<S, T> proxiedApplicationService,
            AuthService authService,
            AuthData authData) {
        this.proxiedApplicationService = proxiedApplicationService;
        this.authService = authService;
        this.authData = authData;
    }

    @Override
    public S execute(T request) {
        authService.execute(authData);
        return proxiedApplicationService.execute(request);
    }
}
