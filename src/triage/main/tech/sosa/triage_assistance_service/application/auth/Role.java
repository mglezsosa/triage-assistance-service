package tech.sosa.triage_assistance_service.application.auth;

import javax.servlet.http.HttpServletRequest;

public interface Role {

    String getName();
    boolean isAuthorized(HttpServletRequest request);

}
