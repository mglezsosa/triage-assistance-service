package tech.sosa.triage_assistance_service.identity_access.application;

import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.identity_access.application.service.Authorize;
import tech.sosa.triage_assistance_service.identity_access.application.service.AuthorizeRequest;
import tech.sosa.triage_assistance_service.identity_access.domain.model.AuthService;
import tech.sosa.triage_assistance_service.identity_access.domain.model.InvalidCredentialsException;
import tech.sosa.triage_assistance_service.identity_access.domain.model.NotAuthorizedException;
import tech.sosa.triage_assistance_service.identity_access.port.adapter.HardcodedAuthService;

public class AuthorizeShould {

    private AuthService authService;

    private static final String USE_CASE_NOT_AUTHORIZED_FOR_TELEPHONE_OPERATORS = "tech.sosa.triage_assistance_service.triage_evaluations.application.service.FullyEvaluate";

    private static final String TOKEN_FOR_ADMIN_USER = "token-admin";
    private static final String TOKEN_FOR_HEALTH_PROFESSIONAL_USER = "token-health-professional";
    private static final String TOKEN_FOR_TELEPHONE_OPERATOR_USER = "token-telephone-operator";

    @Before
    public void setUp() {
        this.authService = new HardcodedAuthService();
    }

    @Test
    public void grantAccessToAnAdminUserForAnHealthProfessionalExclusiveUseCase() {

        new Authorize(authService).execute(new AuthorizeRequest(
                TOKEN_FOR_ADMIN_USER,
                USE_CASE_NOT_AUTHORIZED_FOR_TELEPHONE_OPERATORS,
                "request info..."));
    }

    @Test
    public void grantAccessToAnHealthProfessionalUserForAnHealthProfessionalExclusiveUseCase() {

        new Authorize(authService).execute(new AuthorizeRequest(
                TOKEN_FOR_HEALTH_PROFESSIONAL_USER,
                USE_CASE_NOT_AUTHORIZED_FOR_TELEPHONE_OPERATORS,
                "request info..."));
    }

    @Test(expected = NotAuthorizedException.class)
    public void notGrantAccessToAnTelephoneOperatorUserForAnHealthProfessionalExclusiveUseCase() {

        new Authorize(authService).execute(new AuthorizeRequest(
                TOKEN_FOR_TELEPHONE_OPERATOR_USER,
                USE_CASE_NOT_AUTHORIZED_FOR_TELEPHONE_OPERATORS,
                "request info..."));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void failIfCredentialsAreNotValid() {

        new Authorize(authService).execute(new AuthorizeRequest(
                "wrong-credentials",
                USE_CASE_NOT_AUTHORIZED_FOR_TELEPHONE_OPERATORS,
                "request info..."));
    }

}
