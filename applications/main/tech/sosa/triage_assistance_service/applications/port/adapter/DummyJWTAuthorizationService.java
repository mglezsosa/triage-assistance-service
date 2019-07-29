package tech.sosa.triage_assistance_service.applications.port.adapter;

import tech.sosa.triage_assistance_service.applications.application.AuthorizationData;
import tech.sosa.triage_assistance_service.applications.application.AuthorizationService;
import tech.sosa.triage_assistance_service.identity_access.application.service.Authorize;
import tech.sosa.triage_assistance_service.identity_access.application.service.AuthorizeRequest;

public class DummyJWTAuthorizationService implements AuthorizationService {

    private final Authorize authorizeUseCase;

    public DummyJWTAuthorizationService(Authorize authorizeUseCase) {
        this.authorizeUseCase = authorizeUseCase;
    }

    @Override
    public void execute(AuthorizationData authData) {
        authorizeUseCase.execute((AuthorizeRequest) authData);
    }
}
