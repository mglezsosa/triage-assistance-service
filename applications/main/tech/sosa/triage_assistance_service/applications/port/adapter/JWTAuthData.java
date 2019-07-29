package tech.sosa.triage_assistance_service.applications.port.adapter;

import tech.sosa.triage_assistance_service.applications.application.AuthData;
import tech.sosa.triage_assistance_service.identity_access.application.service.AuthorizeRequest;

public class JWTAuthData extends AuthorizeRequest implements AuthData {

    public JWTAuthData(String authData, String useCaseName, String request) {
        super(authData, useCaseName, request);
    }

    public static JWTAuthData fromAuthorizationHeaderString(String header, String useCaseName, String request) {
        assert header.startsWith("Bearer ");
        String token = header.substring(7);
        return new JWTAuthData(
                token,
                useCaseName,
                request
        );
    }
}
