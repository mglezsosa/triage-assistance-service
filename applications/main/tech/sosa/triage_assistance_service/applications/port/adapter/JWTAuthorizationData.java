package tech.sosa.triage_assistance_service.applications.port.adapter;

import tech.sosa.triage_assistance_service.applications.application.AuthorizationData;
import tech.sosa.triage_assistance_service.identity_access.application.service.AuthorizeRequest;

public class JWTAuthorizationData extends AuthorizeRequest implements AuthorizationData {

    public JWTAuthorizationData(String authData, String useCaseName, String request) {
        super(authData, useCaseName, request);
    }

    public static JWTAuthorizationData fromAuthorizationHeaderString(String header, String useCaseName, String request) {
        assert header.startsWith("Bearer ");
        String token = header.substring(7);
        return new JWTAuthorizationData(
                token,
                useCaseName,
                request
        );
    }
}
