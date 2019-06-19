package tech.sosa.triage_assistance_service.application.auth;

import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

public class HealthProfessionalRole implements Role {

    private Collection<AuthorizableHTTPRequest> authorizedRequests = Arrays.asList(
            new AuthorizableHTTPRequest("/triage/", "GET"),
            new AuthorizableHTTPRequest("/triage/critical-only", "POST"),
            new AuthorizableHTTPRequest("/triage/full", "POST"),
            new AuthorizableHTTPRequest("/triage/next-enqueued", "GET")
    );

    @Override
    public String getName() {
        return "health-professional";
    }

    @Override
    public boolean isAuthorized(HttpServletRequest request) {
        return false;
    }
}
