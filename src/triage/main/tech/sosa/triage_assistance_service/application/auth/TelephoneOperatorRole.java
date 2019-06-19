package tech.sosa.triage_assistance_service.application.auth;

import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

public class TelephoneOperatorRole implements Role {

    private Collection<AuthorizableHTTPRequest> authorizedRequests = Arrays.asList(
            new AuthorizableHTTPRequest("/triage/", "GET"),
            new AuthorizableHTTPRequest("/triage/critical-only", "POST")
    );

    @Override
    public String getName() {
        return "telephone-operator";
    }

    @Override
    public boolean isAuthorized(HttpServletRequest request) {
        AuthorizableHTTPRequest thisRequest = new AuthorizableHTTPRequest(request.getRequestURI(), request.getMethod());
        return authorizedRequests.contains(thisRequest);
    }
}
