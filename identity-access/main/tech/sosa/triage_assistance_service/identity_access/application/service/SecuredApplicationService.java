package tech.sosa.triage_assistance_service.identity_access.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;

public class SecuredApplicationService<S, T extends ApplicationRequest> implements ApplicationService<S, T> {

    private ApplicationService<S, T> proxiedApplicationService;
    private Authorize authService;
    private AuthorizeRequest authRequest;

    public SecuredApplicationService(
            ApplicationService<S, T> proxiedApplicationService,
            Authorize authService,
            AuthorizeRequest authRequest) {
        this.proxiedApplicationService = proxiedApplicationService;
        this.authService = authService;
        this.authRequest = authRequest;
    }

    @Override
    public S execute(T request) {

        authService.execute(authRequest);

        return proxiedApplicationService.execute(request);
    }
}
