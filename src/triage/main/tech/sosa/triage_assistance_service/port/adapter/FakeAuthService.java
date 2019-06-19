package tech.sosa.triage_assistance_service.port.adapter;

import javax.servlet.http.HttpServletRequest;
import tech.sosa.triage_assistance_service.application.auth.AdminRole;
import tech.sosa.triage_assistance_service.application.auth.AuthService;
import tech.sosa.triage_assistance_service.application.auth.User;

public class FakeAuthService implements AuthService {

    @Override
    public User authenticate(HttpServletRequest request) {
        return new User("random-id", new AdminRole());
    }
}
