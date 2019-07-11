package tech.sosa.triage_assistance_service.identity_access.domain.model;

import java.util.Optional;

public abstract class AuthService {

    public abstract Optional<User> authenticate(Credentials credentials);

}