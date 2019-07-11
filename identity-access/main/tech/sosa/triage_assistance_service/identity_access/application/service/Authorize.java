package tech.sosa.triage_assistance_service.identity_access.application.service;

import tech.sosa.triage_assistance_service.identity_access.domain.model.*;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;

public class Authorize implements ApplicationService<Void, AuthorizeRequest> {

    private AuthService authService;

    public Authorize(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Void execute(AuthorizeRequest request) {

        User activeUser = authService.authenticate(new Credentials(request.authData))
                .orElseThrow(InvalidCredentialsException::new);

        if (!activeUser.hasAuthorization(request.useCaseName, request.request)) {
            throw new NotAuthorizedException();
        }

        return null;
    }
}
