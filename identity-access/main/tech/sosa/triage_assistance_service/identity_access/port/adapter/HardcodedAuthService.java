package tech.sosa.triage_assistance_service.identity_access.port.adapter;

import tech.sosa.triage_assistance_service.identity_access.domain.model.*;

import java.util.Optional;

public class HardcodedAuthService extends AuthService {

    @Override
    public Optional<User> authenticate(Credentials credentials) {
        switch (credentials.codedInfo()) {
            case "token-admin":
                return Optional.of(new User("random-id", new AdminRole()));
            case "token-health-professional":
                return Optional.of(new User("another-random-id", new HealthProfessionalRole()));
            case "token-telephone-operator":
                return Optional.of(new User("yet-another-random-id", new TelephoneOperatorRole()));
            default:
                return Optional.empty();
        }
    }

}
