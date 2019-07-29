package tech.sosa.triage_assistance_service.applications.port.adapter;

import tech.sosa.triage_assistance_service.applications.application.AuthData;
import tech.sosa.triage_assistance_service.applications.application.AuthService;
import tech.sosa.triage_assistance_service.identity_access.application.service.Authorize;
import tech.sosa.triage_assistance_service.identity_access.application.service.AuthorizeRequest;

public class DummyJWTAuthService implements AuthService {

    private final Authorize authorizeUseCase;

    public DummyJWTAuthService(Authorize authorizeUseCase) {
        this.authorizeUseCase = authorizeUseCase;
    }

    @Override
    public void execute(AuthData authData) {
        authorizeUseCase.execute((AuthorizeRequest) authData);
    }
}
